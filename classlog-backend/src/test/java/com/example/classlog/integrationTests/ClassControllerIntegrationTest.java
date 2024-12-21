package com.example.classlog.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.classlog.config.UserAuthenticationProvider;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.CreateClassDto;
import com.example.classlog.dto.ManageUserClassRequestDto;
import com.example.classlog.dto.ManageUserClassWithCodeDto;
import com.example.classlog.entities.Class;
import com.example.classlog.entities.Role;
import com.example.classlog.entities.User;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.repository.RoleRepository;
import com.example.classlog.repository.UserClassRepository;
import com.example.classlog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ClassControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ClassRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserClassRepository userClassRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserAuthenticationProvider authProvider;

  private User testUser;
  private Class testClass;
  private String userToken;

  @BeforeEach
  void setUp() {
    userClassRepository.deleteAll();
    classRepository.deleteAll();
    userRepository.deleteAll();
    roleRepository.deleteAll();

    Role role = new Role(1L, "Teacher");
    role = roleRepository.save(role);

    testUser = User.builder()
        .name("John")
        .surname("Doe")
        .email("john.doe@example.com")
        .password("testPassword")
        .role(role)
        .build();

    testUser = userRepository.save(testUser);

    userToken = authProvider.createToken(userMapper.toUserDto(testUser));

    testClass = Class.builder()
        .name("Math Class")
        .code("MATH123")
        .build();

    testClass = classRepository.save(testClass);
  }

  @Test
  void shouldAddClass() throws Exception {
    CreateClassDto createClassDto = CreateClassDto.builder()
        .classDto(ClassDto.builder()
            .name("Science Class")
            .build())
        .createdBy(userMapper.toUserDto(testUser))
        .build();

    mockMvc.perform(post("/classes")
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createClassDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Science Class"));

    List<Class> classes = classRepository.findAll();
    assertThat(classes).hasSize(2);
    assertThat(classes.get(1).getName()).isEqualTo("Science Class");
  }

  @Test
  void shouldGetClassById() throws Exception {
    mockMvc.perform(get("/classes/" + testClass.getId())
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Math Class"));
  }

  @Test
  void shouldAddUserToClassByCode() throws Exception {
    ManageUserClassWithCodeDto requestDto = ManageUserClassWithCodeDto.builder()
        .classCode("MATH123")
        .user(userMapper.toUserDto(testUser))
        .build();

    mockMvc.perform(post("/classes/add/code")
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(content().string("Users successfully added to the class."));

    List<Class> userClasses = classRepository.findByUserId(testUser.getId());
    assertThat(userClasses).hasSize(1);
    assertThat(userClasses.get(0).getName()).isEqualTo("Math Class");
  }

  @Test
  void shouldAddUsersToClass() throws Exception {
    Role role = roleRepository.findById(1L).orElseThrow();

    // Create and save a second user
    User secondUser = User.builder()
        .name("Jane")
        .surname("Smith")
        .email("jane.smith@example.com")
        .password("testPassword")
        .role(role)
        .build();

    secondUser = userRepository.save(secondUser);

    ManageUserClassRequestDto requestDto = ManageUserClassRequestDto.builder()
        .classId(testClass.getId())
        .users(List.of(
            userMapper.toUserDto(testUser),
            userMapper.toUserDto(secondUser)
        ))
        .build();

    mockMvc.perform(post("/classes/add/users")
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(content().string("Users successfully added to the class."));

    assertThat(userClassRepository.existsByClassEntity_IdAndUser_Id(testClass.getId(),
        testUser.getId())).isTrue();
    assertThat(userClassRepository.existsByClassEntity_IdAndUser_Id(testClass.getId(),
        secondUser.getId())).isTrue();
  }

}
