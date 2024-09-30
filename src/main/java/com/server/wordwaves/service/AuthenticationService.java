package com.server.wordwaves.service;


import com.server.wordwaves.dto.request.AuthenticationRequest;
import com.server.wordwaves.dto.request.IntrospectRequest;
import com.server.wordwaves.dto.response.AuthenticationResponse;
import com.server.wordwaves.dto.response.IntrospectResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request);
}
