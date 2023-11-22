package com.alal.backend.advice.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(email + " 이 이메일을 가진 유저를 찾을 수 없습니다.");
    }
}
