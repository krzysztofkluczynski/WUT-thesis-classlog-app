package com.example.classlog.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private long userId;
    String oldPassword;
    String newPassword;
}
