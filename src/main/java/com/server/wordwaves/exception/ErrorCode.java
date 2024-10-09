package com.server.wordwaves.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // UNCATEGORIZED
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // USER ERROR CODE
    INVALID_EMAIL(1001, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_IS_REQUIRED(1002, "Email không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Mật khẩu phải có ít nhất {min}", HttpStatus.BAD_REQUEST),
    WRONG_FORMAT_PASSWORD(
            1004,
            "Mật khẩu phải có tối thiểu 1 chữ số, 1 chữ cái, 1 chữ cái in hoa và 1 kí tự đặc biệt",
            HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Vui lòng đăng nhập để sử dụng tính năng này", HttpStatus.UNAUTHORIZED),
    EMAIL_EXISTED(1006, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1007, "Bạn không thể truy cập tính năng này", HttpStatus.FORBIDDEN),
    USER_NOT_EXISTED(1008, "Tài khoản không tồn tại", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1009, "Mật khẩu không chính xác", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1010, "Tài khoản đã tồn tại", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1011, "Mật khẩu không khớp với xác nhận mật khẩu", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAIL(1012, "Thay đổi ảnh đại diện thất bại", HttpStatus.BAD_REQUEST),

    // TOKEN
    EMPTY_TOKEN(2001, "Token rỗng", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(2002, "Token giải mã không chính xác", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(2003, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    TOKEN_WAS_LOGOUT(2004, "Token đã đăng xuất, vui lòng đăng nhập lại", HttpStatus.BAD_REQUEST),

    // API
    LACK_OF_PARAMETER(3001, "Api còn thiếu tham số", HttpStatus.BAD_REQUEST),

    // ROLE
    ROLE_NOT_EXIST(4001, "Tác nhân không tồn tại", HttpStatus.BAD_REQUEST),

    // FILE
    EMPTY_FILE(5001, "File rỗng", HttpStatus.BAD_REQUEST),

    // VOCABULARY
    WORD_COLLECTION_NAME_IS_REQUIRED(6001, "Tên của bộ từ vựng không được để trống", HttpStatus.BAD_REQUEST),
    COLLECTION_EXISTED(6002, "Bộ từ vựng đã tồn tại" , HttpStatus.BAD_REQUEST)

    ;
    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
