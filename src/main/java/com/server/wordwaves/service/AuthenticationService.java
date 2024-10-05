package com.server.wordwaves.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import com.nimbusds.jose.JOSEException;
import com.server.wordwaves.dto.request.AuthenticationRequest;
import com.server.wordwaves.dto.request.IntrospectRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.IntrospectResponse;

public interface AuthenticationService {
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    ResponseEntity<Void> logout(String token);

    ResponseEntity<AuthenticationResponse> getRefreshToken(String refreshToken) throws ParseException, JOSEException;
}
