package com.mydoctor.recruitmentassignment.common.exception;

import com.mydoctor.recruitmentassignment.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> exceptionHandler(Exception e) {
        log.error("INTERNAL SERVER ERROR: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException -> errorCode: {}, errorMessage: {}", e.getErrorCode(), e.getMessage());

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.fail(e.getMessage(), errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("유효하지 않은 요청입니다. 속성명, 타입, 값을 확인해 주세요.", ErrorCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<CommonResponse> handleInvalidParameterException(InvalidParameterException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("유효하지 않은 요청입니다. 속성명, 타입, 값을 확인해 주세요.", ErrorCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("유효하지 않은 요청입니다. 속성명, 타입, 값을 확인해 주세요.", ErrorCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail("유효하지 않은 요청입니다. 속성명, 타입, 값을 확인해 주세요.", ErrorCode.INVALID_PARAMETER));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.fail("지원하지 않는 Endpoint입니다. URI, Method를 확인해 주세요.", ErrorCode.NOT_FOUND));
    }
}

