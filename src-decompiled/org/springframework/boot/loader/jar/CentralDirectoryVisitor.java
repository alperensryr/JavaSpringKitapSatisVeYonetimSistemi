/*
 * Decompiled with CFR 0.151.
 */
package org.springframework.boot.loader.jar;

import org.springframework.boot.loader.data.RandomAccessData;
import org.springframework.boot.loader.jar.CentralDirectoryEndRecord;
import org.springframework.boot.loader.jar.CentralDirectoryFileHeader;

interface CentralDirectoryVisitor {
    public void visitStart(CentralDirectoryEndRecord var1, RandomAccessData var2);

    public void visitFileHeader(CentralDirectoryFileHeader var1, long var2);

    public void visitEnd();
}

