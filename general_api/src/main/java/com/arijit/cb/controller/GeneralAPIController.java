package com.arijit.cb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralAPIController<T> {

    @GetMapping("/api/rest1/")
    public ResponseEntity<T> getResponse1(){
        return (ResponseEntity<T>) ResponseEntity.ok().body("Rest1 is good");
    }

    @GetMapping("/api/rest2/")
    public ResponseEntity<T> getResponse2(){
        return (ResponseEntity<T>) ResponseEntity.ok().body("Rest2 is good");
    }
}
