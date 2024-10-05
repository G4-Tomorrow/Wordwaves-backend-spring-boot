package com.server.wordwaves.service;

import com.server.wordwaves.dto.ApiResponse;
import org.springframework.http.ResponseEntity;

import com.server.wordwaves.dto.request.AuthenticationRequest;
import com.server.wordwaves.dto.request.IntrospectRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.IntrospectResponse;

public interface AuthenticationService {
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);

    ResponseEntity<ApiResponse<Void>> logout(String token);
}
