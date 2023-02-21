package com.arijit.cb.controller;

import com.arijit.cb.service.CBImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CBController<T> {

    @Autowired
    private CBImplementor cbImplementor;

    @GetMapping("/rest1")
    public ResponseEntity<T> getRest1(){
        String body = cbImplementor.rest1Impelmentor();
        return (ResponseEntity<T>) ResponseEntity.ok().body(body);
    }

    @GetMapping("/rest2")
    public ResponseEntity<T> getRest2(){
        String body = cbImplementor.rest2Impelmentor();
        return (ResponseEntity<T>) ResponseEntity.ok().body(body);
    }
}
