package com.coherent.unnamed.logic.Constants;/*
 *
 * Copyright (c) 2019 Coherent Limited
 *
 * All information contained herein is, and remains the property of Coherent
 * Limited. The intellectual and technical concepts contained herein are
 * proprietary to Coherent and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is
 * strictly forbidden unless prior written permission is obtained from Coherent
 * Limited
 *
 */

public enum ErrorCode implements ErrorHandle {


    CAP_1003("1004", "CheckString mismatch");

    private final String errorCode;
    private final String message;

    ErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}