package com.coherent.unnamed.logic.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private int id;

    private String firstName;

    private String fullName;

    private String emailId;

    private String username;

    private String gguId;

    private String lastName;

    private String displayUserId;

    private String mobileNo;

    private Integer regionIds;

    private Integer roleId;

    private Integer isSuperAdmin;

    private Integer isRoleAdmin;

    private Integer isActive = 1;

    private Integer deletedFlag = 0;

    private Integer createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Integer modifiedBy;

   /* private RoleDTO role;

    private CountryDTO country;*/

}
