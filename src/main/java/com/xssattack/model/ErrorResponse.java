package com.xssattack.model;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String message;
}
