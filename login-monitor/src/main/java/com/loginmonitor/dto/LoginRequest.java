
package com.loginmonitor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.loginmonitor.entity.LoginStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 100)
    private String username;

    @NotBlank(message = "IP address is required")
    @Pattern(
        regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})\\.){3}(?:25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})$|^(?:[0-9a-fA-F]{1,4}:){2,7}[0-9a-fA-F]{0,4}$",
        message = "Invalid IP address"
    )
    private String ipAddress;

    @NotNull(message = "Login status is required")
    private LoginStatus status;
}
