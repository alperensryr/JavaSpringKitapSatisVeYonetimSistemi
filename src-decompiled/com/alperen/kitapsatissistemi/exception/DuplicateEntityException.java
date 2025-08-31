/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.DuplicateEntityException
 */
package com.alperen.kitapsatissistemi.exception;

import com.alperen.kitapsatissistemi.exception.BusinessException;

public class DuplicateEntityException
extends BusinessException {
    public DuplicateEntityException(String entityName, String fieldName, Object value) {
        super(String.format("%s zaten mevcut - %s: %s", entityName, fieldName, value));
    }

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}

