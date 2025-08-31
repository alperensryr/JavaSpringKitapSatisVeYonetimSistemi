/*
 * Decompiled with CFR 0.151.
 */
package org.springframework.boot.loader.jarmode;

public interface JarMode {
    public boolean accepts(String var1);

    public void run(String var1, String[] var2);
}

