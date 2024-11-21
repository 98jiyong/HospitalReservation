package com.asklepios.hospitalreservation_asklepios.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String user_id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String user_password;

    @Builder
    public UserDto(String user_id, String user_password) {
        this.user_id = user_id;
        this.user_password = user_password;
    }

}
