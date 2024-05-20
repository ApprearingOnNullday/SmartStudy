package com.michelle.smartstudy.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    // 用户id
    private int id;

    // 用户名
    private String username;

    // 用户角色
    private int role;
}
