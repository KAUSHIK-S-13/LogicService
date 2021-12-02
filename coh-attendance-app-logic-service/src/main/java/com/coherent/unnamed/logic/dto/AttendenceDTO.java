package com.coherent.unnamed.logic.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data

public class AttendenceDTO {

    private String isPresent;
    private Timestamp createdAt;
}
