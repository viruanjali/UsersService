package com.amsidh.mvc.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateUserRequestModel {

    private String userId;

    @NotNull(message = "Password must not be null")
    @Size(min = 8, max = 16, message = "Password must be more than 8 and less than 16 characters")
    private String password;

    @NotNull(message = "First name must not be null")
    @Size(min = 2, max = 50, message = "First name must be more than 2 and less than 50 characters")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @Size(min = 2, max = 50, message = "Last name must be more than 2 and less than 50 characters")
    private String lastName;

    @NotNull(message = "Email Id must not be null")
    @Email(message = "Email Id must be valid")
    private String emailId;
}
