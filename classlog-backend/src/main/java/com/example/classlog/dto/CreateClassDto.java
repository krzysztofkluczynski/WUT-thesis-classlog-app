package com.example.classlog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateClassDto {
    UserDto createdBy;
    ClassDto classDto;
}
