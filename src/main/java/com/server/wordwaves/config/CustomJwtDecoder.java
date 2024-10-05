package com.server.wordwaves.config;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import com.server.wordwaves.exception.AppException;
import com.server.wordwaves.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.server.wordwaves.service.BaseRedisService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    BaseRedisService baseRedisService;

    @NonFinal
    @Value("${jwt.access-signer-key}")
    private String signerKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        boolean checkedToken = baseRedisService.exist(token);

        if (checkedToken) throw new JwtException("Invalid Token");

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            var jwt = new Jwt(
                    token,
                    jwtClaimsSet.getIssueTime().toInstant(),
                    jwtClaimsSet.getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    jwtClaimsSet.getClaims());
            Instant expiryTime = jwt.getExpiresAt();

            if (expiryTime != null && expiryTime.isBefore(Instant.now())) {
                throw new JwtException("Token has expired");
            }

            return jwt;
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
