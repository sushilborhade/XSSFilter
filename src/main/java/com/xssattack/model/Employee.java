package com.xssattack.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class Employee {

    private String id;

    private String name;

    private String role;

    private int age;

    private List<Address> address;

    private Set<String> mobileNumber;

    private Map<String,Long>  vehicleList;

}
