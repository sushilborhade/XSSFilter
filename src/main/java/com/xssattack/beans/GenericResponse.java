package com.xssattack.beans;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
@Data
@ToString
public class GenericResponse {

    private HashMap<String,Object> payload;
    private Integer status;
    private String message;
}