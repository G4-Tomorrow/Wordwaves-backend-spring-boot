package com.server.wordwaves.service.implement;

import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import com.server.wordwaves.service.TokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenServiceImp implements TokenService {

    @Override
    public void ensureTokenPresent(String token) {
        if (token == null || token.isEmpty()) throw new AppException(ErrorCode.EMPTY_TOKEN);
    }
}
