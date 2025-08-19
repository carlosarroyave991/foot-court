package com.foorcourt.domain.model.feignclient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private LocalDate birthday;
    private String phone;
    private Long dni;
    private RoleModel role;
}
