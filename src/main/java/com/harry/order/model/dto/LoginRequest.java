package com.harry.order.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class LoginRequest {
    @NotBlank(message = "loginId cannot be blank")
    private String loginId;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;
}
