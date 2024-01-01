package com.catarot.server.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @NotEmpty(message = "Please enter your username")
    @Pattern(regexp = "^[a-zA-Z]*$]", message="Username can only contain alphabets a-z and/or A-Z")
    @Size(min=4, max=30)
    private String username;

    @NotEmpty(message = "Please enter your password")
    @Pattern(regexp = "^[a-zA-Z0-9]*$]", message="Password can only contain alphabets a-z, A-Z and/or 0-9")
    @Size(min=6, max=16)
    private String password;
}
