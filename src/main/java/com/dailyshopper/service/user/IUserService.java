package com.dailyshopper.service.user;

import com.dailyshopper.dto.UserDto;
import com.dailyshopper.model.User;
import com.dailyshopper.request.CreateUserRequest;
import com.dailyshopper.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);


    UserDto convertUserToDto(User user);
}
