package com.coherent.unnamed.logic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserContextDTO implements Serializable {

    private static final long serialVersionUID = 7545340461305338564L;

    private int id;

    private String username;

    private Integer isActive;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialsExpired;

    private int tenantId;

    private String authorization;

    private String imageUrl;

    private String fullName;

    private long currentDate;

    private long iat;

    private long exp;

    private String iss;

    private String mobileNumber;

    private String email;

/*    private List<Role> roles;

    private CountryDTO country;*/

    private Integer isRoleAdmin;

    private Integer isSuperAdmin;

}
