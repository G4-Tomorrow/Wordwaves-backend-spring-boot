package com.server.wordwaves.mapper;

import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
}
