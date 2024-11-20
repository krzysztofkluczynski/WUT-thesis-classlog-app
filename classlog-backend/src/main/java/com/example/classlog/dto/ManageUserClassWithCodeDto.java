package com.example.classlog.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ManageUserClassWithCodeDto {
    private String classCode;
    private UserDto user;
}
