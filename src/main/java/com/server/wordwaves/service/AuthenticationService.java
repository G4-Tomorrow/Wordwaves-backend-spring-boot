package com.server.wordwaves.service;

import org.springframework.http.ResponseEntity;

import com.server.wordwaves.dto.request.auth.AuthenticationRequest;
import com.server.wordwaves.dto.request.auth.IntrospectRequest;
import com.server.wordwaves.dto.response.auth.AuthenticationResponse;
import com.server.wordwaves.dto.response.auth.IntrospectResponse;

public interface AuthenticationService {
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);
}
