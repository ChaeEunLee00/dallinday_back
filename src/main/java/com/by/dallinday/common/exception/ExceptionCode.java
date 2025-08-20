package com.by.dallinday.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    COURSE_NOT_FOUND(404, "Course not found"),
    RUN_NOT_FOUND(404, "Run not found"),
    FAVORITE_NOT_FOUND(404, "Favorite not found"),
    COURSE_ID_EXIST(409, "Course id exists"),
    COURSE_NAME_EXIST(409, "Course name exists"),
    MEMBER_EXIST(409, "Member exists"),
    FAVORITE_EXIST(409, "Favorite exists"),
    PROVIDER_NOT_FOUND(404, "The OAuth provider is not supported"),
    EXTERNAL_API_ERROR(502, "Failed to fetch data from external API"),
    ONLY_ADMIN(404, "Administrator only"),
    DATABASE_ERROR(500, "Database error occurred"),
    GPX_ANALYZE_ERROR(500, "GPX analyze error occurred"),
    PASSWORD_MISMATCH(401, "Password mismatch"),
    FORBIDDEN_NOT_OWNER(403, "You are not the owner of this resource");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
