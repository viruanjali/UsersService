package com.amsidh.mvc.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CreateUserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String emailId;
}
