package com.server.wordwaves.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

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
    USER_NOT_EXISTED(1008, "Tài khoản không tồn tại", HttpStatus.FORBIDDEN),
    WRONG_PASSWORD(1009, "Mật khẩu không chính xác", HttpStatus.FORBIDDEN),

    // JOB ERROR CODE
    TITLE_IS_REQUIRED(2001, "Tiêu đề không được để trống", HttpStatus.BAD_REQUEST),
    LOCATION_IS_REQUIRED(2002, "Vị trí tuyển dụng không được để trống", HttpStatus.BAD_REQUEST),
    JOB_TYPE_IS_REQUIRED(2003, "Loại công việc không được để trống", HttpStatus.BAD_REQUEST),
    DESCRIPTION_IS_REQUIRED(2004, "Mô tả không được để trống", HttpStatus.BAD_REQUEST),
    PAYBY_IS_REQUIRED(2005, "Phương thức thanh toán không được để trống", HttpStatus.BAD_REQUEST),
    MIN_AMOUNT_IS_REQUIRED(2006, "Số tiền tối thiểu không được để trống", HttpStatus.BAD_REQUEST),
    PAY_RATE_IS_REQUIRED(2007, "Chu kì không được để trống", HttpStatus.BAD_REQUEST),
    MIN_HOURS_IS_REQIRED(2008, "Số giờ tối thiểu không được để trống", HttpStatus.BAD_REQUEST),
    POSITION_IS_REQUIRED(2009, "Chức danh không được để trống", HttpStatus.BAD_REQUEST),
    QUANTITY_IS_REQUIRED(2010, "Số lượng không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_APPLY_START_DATE(2011, "Ngày bắt đầu tuyển dụng phải là ngày tính từ hôm nay trở đi", HttpStatus.BAD_REQUEST),
    INVALID_APPLY_END_DATE(2012, "Ngày kết thúc tuyển dụng phải sau ngày bắt đầu ít nhất 1 ngày", HttpStatus.BAD_REQUEST),
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
