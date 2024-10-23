package com.server.wordwaves.exception;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;

import com.server.wordwaves.dto.request.vocabulary.WordCreationRequest;
import com.server.wordwaves.dto.response.vocabulary.WordResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalRecoverHandler {
    @Recover
    public WordResponse recoverForCreate(ObjectOptimisticLockingFailureException e, WordCreationRequest request) {
        log.error("Lỗi bất đồng bộ khi cố gắng thêm từ vựng vào chủ đề: {}", request);
        throw new AppException(ErrorCode.CONCURRENT_ADD_WORD_INTO_TOPIC);
    }
}
