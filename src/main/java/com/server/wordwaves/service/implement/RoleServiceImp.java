package com.server.wordwaves.service.implement;

import com.server.wordwaves.mapper.RoleMapper;
import com.server.wordwaves.repository.RoleRepository;
import com.server.wordwaves.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
}
