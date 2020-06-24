package com.amsidh.mvc.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "UserDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USERID", nullable = false, unique = true)
    private String userId;

    @Column(name = "FIRSTNAME", length = 50, nullable = false)
    private String firstName;

    @Column(name = "LASTNAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "EMAILID", length = 120, nullable = false, unique = true)
    private String emailId;

    @Column(name = "PASSWORD", nullable = false)
    private String encryptedPassword;

}
