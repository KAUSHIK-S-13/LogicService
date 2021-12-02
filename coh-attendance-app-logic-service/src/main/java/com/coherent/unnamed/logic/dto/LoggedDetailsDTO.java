package com.coherent.unnamed.logic.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LoggedDetailsDTO {
    public int isLogged;
    public String createdBy;
    public Timestamp createdAt;
}
