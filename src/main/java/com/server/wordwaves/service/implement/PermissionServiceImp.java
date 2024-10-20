package com.server.wordwaves.service.implement;

import com.server.wordwaves.repository.PermissionRepository;
import com.server.wordwaves.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImp implements PermissionService {
    PermissionRepository permissionRepository;
}
