package com.arijit.cb.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CBImplementor {

    final static AtomicBoolean rest1CBOpen = new AtomicBoolean(Boolean.FALSE);
    final static AtomicBoolean rest2CBOpen = new AtomicBoolean(Boolean.FALSE);
    final static AtomicInteger doRest1SyncTrig = new AtomicInteger(0);
    final static AtomicInteger doRest2SyncTrig = new AtomicInteger(0);
    @Autowired
    private CBAsyncHandler cbAsyncHandler;
    @Autowired
    private CBAsyncHandler2 cbAsyncHandler2;

    @CircuitBreaker(name = "rest1lookup")
    public String rest1Impelmentor() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8089/api/rest1/";
        if (doRest1SyncTrig.get() == 0 && !rest2CBOpen.get()) {
            doRest1SyncTrig.getAndIncrement();
            log.info("******************** Rest1 CBM about to be called {} ******************",doRest1SyncTrig.get());
            cbAsyncHandler.evaluateRest1CBstate(doRest1SyncTrig);
        }
        //cbAsyncHandler.evaluateCBState("rest1lookup");
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    @CircuitBreaker(name = "rest2lookup")
    public String rest2Impelmentor() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8091/api/rest2/";
        if (doRest2SyncTrig.get() == 0 && !rest1CBOpen.get()) {
            doRest2SyncTrig.getAndIncrement();
            log.info("******************** Rest2 CBM about to be called {} ******************",doRest2SyncTrig.get());
            cbAsyncHandler.evaluateRest2CBstate(doRest2SyncTrig);
        }
        //cbAsyncHandler.evaluateCBState("rest2lookup");
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

}
