package com.server.wordwaves.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // UNCATEGORIZED ERROR CODE
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
    NEW_PASSWORD_IS_REQUIRED(1013, "Mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_IS_REQUIRED(1014, "Xác nhận mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),

    // TOKEN ERROR CODE
    EMPTY_TOKEN(2001, "Token rỗng", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(2002, "Token giải mã không chính xác", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(2003, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    TOKEN_WAS_LOGOUT(2004, "Token đã đăng xuất, vui lòng đăng nhập lại", HttpStatus.BAD_REQUEST),

    // API ERROR CODE
    LACK_OF_PARAMETER(3001, "Api còn thiếu tham số", HttpStatus.BAD_REQUEST),

    // ROLE ERROR CODE
    ROLE_EXISTED(4001, "Vai trò đã tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(4002, "Vai trò không tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NAME_IS_REQUIRED(4003, "Tên vai trò không được để trống", HttpStatus.BAD_REQUEST),

    // FILE ERROR CODE
    EMPTY_FILE(5001, "File rỗng", HttpStatus.BAD_REQUEST),

    // VOCABULARY ERROR CODE
    WORD_COLLECTION_NAME_IS_REQUIRED(6001, "Tên của bộ từ vựng không được để trống", HttpStatus.BAD_REQUEST),
    WORD_COLLECTION_CATEGORY_IS_REQUIRED(6002, "Phân loại bộ từ vựng không được để trống", HttpStatus.BAD_REQUEST),
    WORD_NAME_IS_REQUIRED(6003, "Tên của từ vựng không được để trống", HttpStatus.BAD_REQUEST),
    COLLECTION_EXISTED(6004, "Bộ từ vựng đã tồn tại", HttpStatus.BAD_REQUEST),
    WORD_COLLECTION_NOT_EXISTED(6005, "Bộ từ vựng không tồn tại", HttpStatus.BAD_REQUEST),
    WORD_COLLECTION_ID_IS_REQUIRED(6006, "Id của bộ từ vựng không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_WORD_COLLECTION_CATEGORY(6007, "Phân loại bộ từ vựng không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_TOPIC_IDS(6008, "Danh sách các chủ đề không hợp lệ", HttpStatus.BAD_REQUEST),

    TOPIC_NAME_IS_REQUIRED(6101, "Tên chủ đề không được để trống", HttpStatus.BAD_REQUEST),
    TOPIC_NOT_EXISTED(6102, "Chủ đề không tồn tại", HttpStatus.BAD_REQUEST),
    TOPIC_MUST_BELONG_TO_WORD_COLLECTION(6103, "Chủ đề phải thuộc về ít nhất 1 topic", HttpStatus.BAD_REQUEST),
    CONCURRENT_ADD_WORD_INTO_TOPIC(6104, "Xung đột trong quá trình thêm từ vựng vào chủ đề", HttpStatus.CONFLICT),
    TOPIC_ID_IS_REQUIRED(6105, "Id của chủ đề không được bỏ trống", HttpStatus.BAD_REQUEST),
    INVALID_WORD_IDS(6106, "Danh sách các từ vựng không hợp lệ", HttpStatus.BAD_REQUEST),

    WORD_EXISTED(6201, "Từ vựng đã tồn tại", HttpStatus.BAD_REQUEST),
    WORD_NOT_EXISTED(6202, "Từ vựng không tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_WORD(6203, "Từ vựng không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_WORD_ID(6204, "Id của từ vựng không hợp lệ", HttpStatus.BAD_REQUEST),
    WORD_ID_IS_REQUIRED(6204, "Id của từ vựng không được để trống", HttpStatus.BAD_REQUEST),

    WORD_IN_LEARNING_NOT_EXISTED(6301, "Người dùng chưa học từ vựng này", HttpStatus.INTERNAL_SERVER_ERROR),

    // OAUTH2 ERROR CODE
    OAUTH2_USER_EXISTED_WITH_DIFFERENT_PROVIDER(
            7001, "Tài khoản đã được liên kết với nhà cung cấp OAuth2 khác trước đó", HttpStatus.BAD_REQUEST),
    USER_EXISTED_WITH_BASIC_ACCOUNT(
            7002, "Tài khoản đã tồn tại trên hệ thống với email/password", HttpStatus.BAD_REQUEST),
    USER_EXISTED_WITH_OAUTH2(7003, "Tài khoản đã tồn tại với nhà cung cấp OAuth2", HttpStatus.BAD_REQUEST),

    // PERMISSION ERROR CODE
    PERMISSION_NAME_IS_REQUIRED(8001, "Tên quyền hạn không được để trống", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(8002, "Quyền hạn không tồn tại trên hệ thống", HttpStatus.BAD_REQUEST),
    PERMISSION_ALREADY_EXISTED(8003, "Quyền hạn đã tồn tại trong vai trò", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(8004, "Quyền hạn đã tồn tại trên hệ thống", HttpStatus.BAD_REQUEST),

    // PAGINATE ERROR CODE
    INVALID_PAGE_NUMBER(9001, "Số trang phải lớn hơn hoặc bằng 1", HttpStatus.BAD_REQUEST),
    INVALID_PAGE_SIZE(9002, "Kích thước trang phải lớn hơn hoặc bằng 1", HttpStatus.BAD_REQUEST),
    INVALID_SORT_DIRECTION(9003, "Hướng sắp xếp không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_SORT_BY(9004, "Trường sắp xếp không hợp lệ", HttpStatus.BAD_REQUEST);
    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
