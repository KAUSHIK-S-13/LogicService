package com.coherent.unnamed.logic.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data

public class TimeLogsDTO {

    private Long id;
    private Long userIdFk;
    private String longitude;
    private String latitude;
    private int isLogged;
    private Boolean isActive;
    private Boolean deletedFlag;
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String modifiedBy;
}
