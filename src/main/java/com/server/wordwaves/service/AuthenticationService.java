package com.server.wordwaves.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.server.wordwaves.dto.request.auth.LogoutRequest;
import com.server.wordwaves.dto.request.auth.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;

import com.server.wordwaves.dto.request.auth.AuthenticationRequest;
import com.server.wordwaves.dto.request.auth.IntrospectRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.auth.IntrospectResponse;

public interface AuthenticationService {
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    ResponseEntity<Void> logout(LogoutRequest request);


    ResponseEntity<AuthenticationResponse> getRefreshToken(RefreshTokenRequest request) throws ParseException, JOSEException, JOSEException;
}
