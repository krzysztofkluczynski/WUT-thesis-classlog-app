package com.example.classlog.service;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.ChangePasswordDto;
import com.example.classlog.dto.CredentialsDto;
import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.Role;
import com.example.classlog.entities.User;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.repository.RoleRepository;
import com.example.classlog.repository.UserRepository;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;


  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  public UserDto login(CredentialsDto credentialsDto) {
    User user = userRepository.findByEmail(credentialsDto.email())
        .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

    if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
      return userMapper.toUserDto(user);
    }
    throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
  }

  public UserDto register(SignUpDto userDto) {
    Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

    if (optionalUser.isPresent()) {
      throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
    }

    String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    Pattern pattern = Pattern.compile(emailRegex);

    // Check if the email matches the pattern
    if (!pattern.matcher(userDto.email()).matches()) {
      throw new AppException("Invalid email format", HttpStatus.BAD_REQUEST);
    }

    User user = userMapper.signUpToUser(userDto);
    user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

    Role role = roleRepository.findById(4L)
        .orElseThrow(() -> new RuntimeException("Role with ID 4 not found"));
    user.setRole(role);

    User savedUser = userRepository.save(user);

    return userMapper.toUserDto(savedUser);
  }

  public List<UserDto> findAllUsers() {
    return userRepository.findAll().stream()
        .map(userMapper::toUserDto)
        .collect(Collectors.toList());
  }

  public UserDto findByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
    return userMapper.toUserDto(user);
  }

  public List<UserDto> findByRole(Long roleId) {
    return userRepository.findByRoleId(roleId).stream()
        .map(userMapper::toUserDto)
        .collect(Collectors.toList());
  }

  public void deleteUser(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new AppException("User not found", HttpStatus.NOT_FOUND);
    }
    userRepository.deleteById(userId);
  }

  public UserDto findUserById(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    return userMapper.toUserDto(user);
  }

  public UserDto updateUser(Long userId, UserDto userDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

    user.setName(userDto.getName());
    user.setSurname(userDto.getSurname());
    user.setEmail(userDto.getEmail());
    user.setRole(userDto.getRole());

    User updatedUser = userRepository.save(user);
    return userMapper.toUserDto(updatedUser);
  }

  public UserDto changePassword(ChangePasswordDto changePasswordDto) {
    User user = userRepository.findById(changePasswordDto.getUserId())
        .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

    if (!passwordEncoder.matches(CharBuffer.wrap(changePasswordDto.getOldPassword()),
        user.getPassword())) {
      throw new AppException("Invalid old password", HttpStatus.BAD_REQUEST);
    }

    user.setPassword(passwordEncoder.encode(CharBuffer.wrap(changePasswordDto.getNewPassword())));
    User updatedUser = userRepository.save(user);

    return userMapper.toUserDto(updatedUser);
  }

  public List<UserDto> getUsersByClass(long classId) {
    return userRepository.findByClassId(classId).stream()
        .map(userMapper::toUserDto)
        .collect(Collectors.toList());
  }

  public List<UserDto> getUsersNotFromClass(long classId) {
    return userRepository.findUsersNotFromClass(classId).stream()
        .map(userMapper::toUserDto)
        .collect(Collectors.toList());
  }
}