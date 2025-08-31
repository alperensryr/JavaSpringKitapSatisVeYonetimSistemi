/*
 * Decompiled with CFR 0.151.
 */
package org.springframework.boot.loader.jar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import org.springframework.boot.loader.data.RandomAccessData;
import org.springframework.boot.loader.jar.AsciiBytes;
import org.springframework.boot.loader.jar.Bytes;
import org.springframework.boot.loader.jar.CentralDirectoryEndRecord;
import org.springframework.boot.loader.jar.CentralDirectoryFileHeader;
import org.springframework.boot.loader.jar.CentralDirectoryVisitor;
import org.springframework.boot.loader.jar.FileHeader;
import org.springframework.boot.loader.jar.JarEntry;
import org.springframework.boot.loader.jar.JarEntryCertification;
import org.springframework.boot.loader.jar.JarEntryFilter;
import org.springframework.boot.loader.jar.JarFile;
import org.springframework.boot.loader.jar.ZipInflaterInputStream;

class JarFileEntries
implements CentralDirectoryVisitor,
Iterable<JarEntry> {
    private static final Runnable NO_VALIDATION;
    private static final String META_INF_PREFIX = "META-INF/";
    private static final Attributes.Name MULTI_RELEASE;
    private static final int BASE_VERSION = 8;
    private static final int RUNTIME_VERSION;
    private static final long LOCAL_FILE_HEADER_SIZE = 30L;
    private static final char SLASH = '/';
    private static final char NO_SUFFIX = '\u0000';
    protected static final int ENTRY_CACHE_SIZE = 25;
    private final JarFile jarFile;
    private final JarEntryFilter filter;
    private RandomAccessData centralDirectoryData;
    private int size;
    private int[] hashCodes;
    private Offsets centralDirectoryOffsets;
    private int[] positions;
    private Boolean multiReleaseJar;
    private JarEntryCertification[] certifications;
    private final Map<Integer, FileHeader> entriesCache = Collections.synchronizedMap(new LinkedHashMap<Integer, FileHeader>(16, 0.75f, true){

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, FileHeader> eldest) {
            return this.size() >= 25;
        }
    });

    JarFileEntries(JarFile jarFile, JarEntryFilter filter) {
        this.jarFile = jarFile;
        this.filter = filter;
        if (RUNTIME_VERSION == 8) {
            this.multiReleaseJar = false;
        }
    }

    @Override
    public void visitStart(CentralDirectoryEndRecord endRecord, RandomAccessData centralDirectoryData) {
        int maxSize = endRecord.getNumberOfRecords();
        this.centralDirectoryData = centralDirectoryData;
        this.hashCodes = new int[maxSize];
        this.centralDirectoryOffsets = Offsets.from(endRecord);
        this.positions = new int[maxSize];
    }

    @Override
    public void visitFileHeader(CentralDirectoryFileHeader fileHeader, long dataOffset) {
        AsciiBytes name = this.applyFilter(fileHeader.getName());
        if (name != null) {
            this.add(name, dataOffset);
        }
    }

    private void add(AsciiBytes name, long dataOffset) {
        this.hashCodes[this.size] = name.hashCode();
        this.centralDirectoryOffsets.set(this.size, dataOffset);
        this.positions[this.size] = this.size;
        ++this.size;
    }

    @Override
    public void visitEnd() {
        this.sort(0, this.size - 1);
        int[] positions = this.positions;
        this.positions = new int[positions.length];
        for (int i = 0; i < this.size; ++i) {
            this.positions[positions[i]] = i;
        }
    }

    int getSize() {
        return this.size;
    }

    private void sort(int left, int right) {
        if (left < right) {
            int pivot = this.hashCodes[left + (right - left) / 2];
            int i = left;
            int j = right;
            while (i <= j) {
                while (this.hashCodes[i] < pivot) {
                    ++i;
                }
                while (this.hashCodes[j] > pivot) {
                    --j;
                }
                if (i > j) continue;
                this.swap(i, j);
                ++i;
                --j;
            }
            if (left < j) {
                this.sort(left, j);
            }
            if (right > i) {
                this.sort(i, right);
            }
        }
    }

    private void swap(int i, int j) {
        JarFileEntries.swap(this.hashCodes, i, j);
        this.centralDirectoryOffsets.swap(i, j);
        JarFileEntries.swap(this.positions, i, j);
    }

    @Override
    public Iterator<JarEntry> iterator() {
        return new EntryIterator(NO_VALIDATION);
    }

    Iterator<JarEntry> iterator(Runnable validator) {
        return new EntryIterator(validator);
    }

    boolean containsEntry(CharSequence name) {
        return this.getEntry(name, FileHeader.class, true) != null;
    }

    JarEntry getEntry(CharSequence name) {
        return this.getEntry(name, JarEntry.class, true);
    }

    InputStream getInputStream(String name) throws IOException {
        FileHeader entry = this.getEntry(name, FileHeader.class, false);
        return this.getInputStream(entry);
    }

    InputStream getInputStream(FileHeader entry) throws IOException {
        if (entry == null) {
            return null;
        }
        InputStream inputStream = this.getEntryData(entry).getInputStream();
        if (entry.getMethod() == 8) {
            inputStream = new ZipInflaterInputStream(inputStream, (int)entry.getSize());
        }
        return inputStream;
    }

    RandomAccessData getEntryData(String name) throws IOException {
        FileHeader entry = this.getEntry(name, FileHeader.class, false);
        if (entry == null) {
            return null;
        }
        return this.getEntryData(entry);
    }

    private RandomAccessData getEntryData(FileHeader entry) throws IOException {
        RandomAccessData data = this.jarFile.getData();
        byte[] localHeader = data.read(entry.getLocalHeaderOffset(), 30L);
        long nameLength = Bytes.littleEndianValue(localHeader, 26, 2);
        long extraLength = Bytes.littleEndianValue(localHeader, 28, 2);
        return data.getSubsection(entry.getLocalHeaderOffset() + 30L + nameLength + extraLength, entry.getCompressedSize());
    }

    private <T extends FileHeader> T getEntry(CharSequence name, Class<T> type, boolean cacheEntry) {
        T entry = this.doGetEntry(name, type, cacheEntry, null);
        if (!this.isMetaInfEntry(name) && this.isMultiReleaseJar()) {
            AsciiBytes nameAlias;
            AsciiBytes asciiBytes = nameAlias = entry instanceof JarEntry ? ((JarEntry)entry).getAsciiBytesName() : new AsciiBytes(name.toString());
            for (int version = RUNTIME_VERSION; version > 8; --version) {
                T versionedEntry = this.doGetEntry("META-INF/versions/" + version + "/" + name, type, cacheEntry, nameAlias);
                if (versionedEntry == null) continue;
                return versionedEntry;
            }
        }
        return entry;
    }

    private boolean isMetaInfEntry(CharSequence name) {
        return name.toString().startsWith(META_INF_PREFIX);
    }

    private boolean isMultiReleaseJar() {
        Boolean multiRelease = this.multiReleaseJar;
        if (multiRelease != null) {
            return multiRelease;
        }
        try {
            Manifest manifest = this.jarFile.getManifest();
            if (manifest == null) {
                multiRelease = false;
            } else {
                Attributes attributes = manifest.getMainAttributes();
                multiRelease = attributes.containsKey(MULTI_RELEASE);
            }
        }
        catch (IOException ex) {
            multiRelease = false;
        }
        this.multiReleaseJar = multiRelease;
        return multiRelease;
    }

    private <T extends FileHeader> T doGetEntry(CharSequence name, Class<T> type, boolean cacheEntry, AsciiBytes nameAlias) {
        int hashCode = AsciiBytes.hashCode(name);
        T entry = this.getEntry(hashCode, name, '\u0000', type, cacheEntry, nameAlias);
        if (entry == null) {
            hashCode = AsciiBytes.hashCode(hashCode, '/');
            entry = this.getEntry(hashCode, name, '/', type, cacheEntry, nameAlias);
        }
        return entry;
    }

    private <T extends FileHeader> T getEntry(int hashCode, CharSequence name, char suffix, Class<T> type, boolean cacheEntry, AsciiBytes nameAlias) {
        for (int index = this.getFirstIndex(hashCode); index >= 0 && index < this.size && this.hashCodes[index] == hashCode; ++index) {
            T entry = this.getEntry(index, type, cacheEntry, nameAlias);
            if (!entry.hasName(name, suffix)) continue;
            return entry;
        }
        return null;
    }

    private <T extends FileHeader> T getEntry(int index, Class<T> type, boolean cacheEntry, AsciiBytes nameAlias) {
        try {
            FileHeader entry;
            long offset = this.centralDirectoryOffsets.get(index);
            FileHeader cached = this.entriesCache.get(index);
            FileHeader fileHeader = entry = cached != null ? cached : CentralDirectoryFileHeader.fromRandomAccessData(this.centralDirectoryData, offset, this.filter);
            if (CentralDirectoryFileHeader.class.equals(entry.getClass()) && type.equals(JarEntry.class)) {
                entry = new JarEntry(this.jarFile, index, (CentralDirectoryFileHeader)entry, nameAlias);
            }
            if (cacheEntry && cached != entry) {
                this.entriesCache.put(index, entry);
            }
            return (T)entry;
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private int getFirstIndex(int hashCode) {
        int index = Arrays.binarySearch(this.hashCodes, 0, this.size, hashCode);
        if (index < 0) {
            return -1;
        }
        while (index > 0 && this.hashCodes[index - 1] == hashCode) {
            --index;
        }
        return index;
    }

    void clearCache() {
        this.entriesCache.clear();
    }

    private AsciiBytes applyFilter(AsciiBytes name) {
        return this.filter != null ? this.filter.apply(name) : name;
    }

    JarEntryCertification getCertification(JarEntry entry) throws IOException {
        JarEntryCertification certification;
        JarEntryCertification[] certifications = this.certifications;
        if (certifications == null) {
            certifications = new JarEntryCertification[this.size];
            try (JarInputStream certifiedJarStream = new JarInputStream(this.jarFile.getData().getInputStream());){
                java.util.jar.JarEntry certifiedEntry = null;
                while ((certifiedEntry = certifiedJarStream.getNextJarEntry()) != null) {
                    certifiedJarStream.closeEntry();
                    int index = this.getEntryIndex(certifiedEntry.getName());
                    if (index == -1) continue;
                    certifications[index] = JarEntryCertification.from(certifiedEntry);
                }
            }
            this.certifications = certifications;
        }
        return (certification = certifications[entry.getIndex()]) != null ? certification : JarEntryCertification.NONE;
    }

    private int getEntryIndex(CharSequence name) {
        int hashCode = AsciiBytes.hashCode(name);
        for (int index = this.getFirstIndex(hashCode); index >= 0 && index < this.size && this.hashCodes[index] == hashCode; ++index) {
            FileHeader candidate = this.getEntry(index, FileHeader.class, false, null);
            if (!candidate.hasName(name, '\u0000')) continue;
            return index;
        }
        return -1;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void swap(long[] array, int i, int j) {
        long temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static {
        int version;
        NO_VALIDATION = () -> {};
        MULTI_RELEASE = new Attributes.Name("Multi-Release");
        try {
            Object runtimeVersion = Runtime.class.getMethod("version", new Class[0]).invoke(null, new Object[0]);
            version = (Integer)runtimeVersion.getClass().getMethod("major", new Class[0]).invoke(runtimeVersion, new Object[0]);
        }
        catch (Throwable ex) {
            version = 8;
        }
        RUNTIME_VERSION = version;
    }

    private static final class Zip64Offsets
    implements Offsets {
        private final long[] offsets;

        private Zip64Offsets(int size) {
            this.offsets = new long[size];
        }

        @Override
        public void swap(int i, int j) {
            JarFileEntries.swap(this.offsets, i, j);
        }

        @Override
        public void set(int index, long value) {
            this.offsets[index] = value;
        }

        @Override
        public long get(int index) {
            return this.offsets[index];
        }
    }

    private static final class ZipOffsets
    implements Offsets {
        private final int[] offsets;

        private ZipOffsets(int size) {
            this.offsets = new int[size];
        }

        @Override
        public void swap(int i, int j) {
            JarFileEntries.swap(this.offsets, i, j);
        }

        @Override
        public void set(int index, long value) {
            this.offsets[index] = (int)value;
        }

        @Override
        public long get(int index) {
            return this.offsets[index];
        }
    }

    private static interface Offsets {
        public void set(int var1, long var2);

        public long get(int var1);

        public void swap(int var1, int var2);

        public static Offsets from(CentralDirectoryEndRecord endRecord) {
            int size = endRecord.getNumberOfRecords();
            return endRecord.isZip64() ? new Zip64Offsets(size) : new ZipOffsets(size);
        }
    }

    private final class EntryIterator
    implements Iterator<JarEntry> {
        private final Runnable validator;
        private int index = 0;

        private EntryIterator(Runnable validator) {
            this.validator = validator;
            validator.run();
        }

        @Override
        public boolean hasNext() {
            this.validator.run();
            return this.index < JarFileEntries.this.size;
        }

        @Override
        public JarEntry next() {
            this.validator.run();
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            int entryIndex = JarFileEntries.this.positions[this.index];
            ++this.index;
            return (JarEntry)JarFileEntries.this.getEntry(entryIndex, JarEntry.class, false, null);
        }
    }
}

