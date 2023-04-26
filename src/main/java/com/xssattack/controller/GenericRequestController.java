package com.xssattack.controller;

import com.xssattack.beans.GenericRequest;
import com.xssattack.beans.GenericResponse;
import com.xssattack.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GenericRequestController {

    @Autowired
    GenericService genericService;

    @RequestMapping(value = "/generic", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> createAndUpdateAgent(@RequestBody GenericRequest request) {
        System.out.println("request accepted ");
        System.out.println(request);
        try {
            GenericResponse response = genericService.processRequest(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}