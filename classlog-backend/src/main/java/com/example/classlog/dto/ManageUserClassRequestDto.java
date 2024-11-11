package com.example.classlog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ManageUserClassRequestDto {
    private Long classId;
    private List<UserDto> users;
}
