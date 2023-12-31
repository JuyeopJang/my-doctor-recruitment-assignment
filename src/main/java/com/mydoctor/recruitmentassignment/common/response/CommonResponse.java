package com.mydoctor.recruitmentassignment.common.response;

import com.mydoctor.recruitmentassignment.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private Result result;
    private T data;
    private String message;
    private ErrorCode errorCode;

    public static <T> CommonResponse<T> success(T data, String message) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.SUCCESS)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> success(String message) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.SUCCESS)
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> success(T data) {
        return success(data, null);
    }

    public static CommonResponse fail(String message, ErrorCode errorCode) {
        CommonResponse<Object> build = CommonResponse.builder()
                .result(Result.FAIL)
                .message(message)
                .errorCode(errorCode)
                .build();
        return build;
    }

    public static CommonResponse fail(ErrorCode errorCode) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(errorCode.getCode())
                .errorCode(errorCode)
                .build();
    }

    public static <T> CommonResponse fail(ErrorCode errorCode, T data) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .errorCode(errorCode)
                .data(data)
                .build();
    }

    public static <T> CommonResponse fail(String message, ErrorCode errorCode, T data) {
        return CommonResponse.builder()
                .result(Result.FAIL)
                .message(message)
                .errorCode(errorCode)
                .data(data)
                .build();
    }

    public enum Result {
        SUCCESS, FAIL
    }
}
