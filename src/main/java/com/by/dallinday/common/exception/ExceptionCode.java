package com.by.dallinday.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    COURSE_NOT_FOUND(404, "Course not found"),
    RUN_NOT_FOUND(404, "Run not found"),
    MEMBER_EXIST(409, "Member exists"),
    ONLY_ADMIN(404, "Administrator only"),
    PROVIDER_NOT_FOUND(404, "The OAuth provider is not supported"),
    DATABASE_ERROR(500, "Database error occurred"),
    EXTERNAL_API_ERROR(502, "Failed to fetch data from external API");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
