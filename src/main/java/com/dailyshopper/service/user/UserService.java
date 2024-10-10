package com.dailyshopper.service.user;

import com.dailyshopper.dto.UserDto;
import com.dailyshopper.exceptions.AlreadyExistsException;
import com.dailyshopper.exceptions.ResourceNotFoundException;
import com.dailyshopper.model.User;
import com.dailyshopper.repository.UserRepository;
import com.dailyshopper.request.CreateUserRequest;
import com.dailyshopper.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository
                        .existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstname(request.getFirstname());
                    user.setLastname(request.getLastname());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(request.getEmail() +" Already exists"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {

        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstname(request.getFirstname());
                    existingUser.setLastname(request.getLastname());
                    return userRepository.save(existingUser);
                }).orElseThrow(()-> new ResourceNotFoundException("user not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository :: delete
                        , () -> {throw new ResourceNotFoundException("User not found");});
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
