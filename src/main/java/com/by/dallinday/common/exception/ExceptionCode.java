package com.by.dallinday.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    COURSE_NOT_FOUND(404, "Course not found"),
    RUN_NOT_FOUND(404, "Run not found"),
    FAVORITE_NOT_FOUND(404, "Favorite not found"),
    MEMBER_EXIST(409, "Member exists"),
    FAVORITE_EXIST(409, "Favorite exists"),
    PROVIDER_NOT_FOUND(404, "The OAuth provider is not supported"),
    EXTERNAL_API_ERROR(502, "Failed to fetch data from external API"),
    ONLY_ADMIN(404, "Administrator only"),
    DATABASE_ERROR(500, "Database error occurred");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
