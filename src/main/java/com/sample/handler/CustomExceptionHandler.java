package com.sample.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 独自のExceptionHandlerクラス.
 * <p>
 * WARNING: 例外ごとのhandleメソッドをOverrideしない場合はAPIレスポンスのHTTPボディが空になる。
 */
@RestControllerAdvice
public final class CustomExceptionHandler
        extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            final Exception ex,
            final Object body,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request) {
        Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
        // 例外が発生した場合はエラーログを必ず出力する。
        logger.error("An exception occurred.", ex);
        return super.handleExceptionInternal(
                ex, body, headers, status, request);
    }

    /**
     * すべての例外をhandleする.
     *
     * @param ex      Exceptionオブジェクト
     * @param request WebRequestオブジェクト
     * @return ResponseEntityオブジェクト
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(
            final Exception ex,
            final WebRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return super.handleExceptionInternal(
                ex,
                null,
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }
}
