package com.xssattack.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Address {

    private String city;

    private String country;

    private String pinCode;


}
