/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 */
package com.alperen.kitapsatissistemi.exception;

import com.alperen.kitapsatissistemi.exception.BusinessException;

public class EntityNotFoundBusinessException
extends BusinessException {
    public EntityNotFoundBusinessException(String entityName, Object id) {
        super(String.format("%s bulunamad\u0131, ID: %s", entityName, id));
    }

    public EntityNotFoundBusinessException(String message) {
        super(message);
    }

    public EntityNotFoundBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

