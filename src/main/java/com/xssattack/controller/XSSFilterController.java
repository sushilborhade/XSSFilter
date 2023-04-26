package com.xssattack.controller;

import com.xssattack.model.Employee;
import org.springframework.web.bind.annotation.*;

@RestController
public class XSSFilterController {

    @GetMapping("/hello/{message}")
    public String show(@PathVariable String message){
        return message;
    }
   @PostMapping("/hello/{message}")
    public String print(@PathVariable String message){
        return message;
    }

    @GetMapping("/welcome")
    public String welcome(@RequestParam String message,@RequestParam String message1){
        return message;
    }

    @PostMapping("/test")
    public String test(@RequestBody Employee employee){
        return "Post call successfully";
    }
}
