package com.server.wordwaves.mapper;

import org.mapstruct.Mapper;

import com.server.wordwaves.dto.request.UserCreationRequest;
import com.server.wordwaves.dto.response.UserResponse;
import com.server.wordwaves.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
