package com.by.dallinday.common.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXIST(409, "Member exists"),
    ONLY_ADMIN(404, "Administrator only"),
    PROVIDER_NOT_FOUND(404, "지원하지 않는 OAuth 입니다.");



    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
