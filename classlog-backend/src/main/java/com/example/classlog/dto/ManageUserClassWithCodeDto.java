package com.example.classlog.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManageUserClassWithCodeDto {

  private String classCode;
  private UserDto user;
}
