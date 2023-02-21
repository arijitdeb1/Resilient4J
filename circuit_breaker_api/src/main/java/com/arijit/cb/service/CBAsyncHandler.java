package com.arijit.cb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class CBAsyncHandler {

    public static final String R4J_STATE_METRIC = "resilience4j.circuitbreaker.state";

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @Async
    public synchronized void evaluateRest1CBstate(AtomicInteger doRest1SyncTrig){
        String lookup = "rest1lookup";
        evaluateCBState(lookup,doRest1SyncTrig);

    }

    @Async
    public synchronized void evaluateRest2CBstate(AtomicInteger doRest2SyncTrig){

        String lookup = "rest2lookup";
        evaluateCBState(lookup, doRest2SyncTrig);
    }


    public void evaluateCBState(String lookup, AtomicInteger doSyncTrig){
        boolean isClosed = false;
        List<String> rest2closedTags = new ArrayList<>();
        rest2closedTags.add("name:"+lookup);
        rest2closedTags.add("state:closed");

        List<String> rest2halfopenTags = new ArrayList<>();
        rest2halfopenTags.add("name:"+lookup);
        rest2halfopenTags.add("state:half_open");

        List<String> rest2openTags = new ArrayList<>();
        rest2openTags.add("name:"+lookup);
        rest2openTags.add("state:open");



        while(!isClosed) {
            log.info("***************** PRINTING {} *****************",lookup);

            try {
                log.info("-------------- {} Waiting ----------------",lookup);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Double rest2closedState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2closedTags).getMeasurements().get(0).getValue();
            Double rest2halfopenState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2halfopenTags).getMeasurements().get(0).getValue();
            Double rest2openState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2openTags).getMeasurements().get(0).getValue();

            log.info("-----rest2closedState:::{}------", rest2closedState);
            log.info("-----rest2halfopenState:::{}------", rest2halfopenState);
            log.info("-----rest2openState:::{}------", rest2openState);

            if(rest2closedState == 1.0){
                isClosed = true;
                if(lookup.equals("rest2lookup")){
                    CBImplementor.rest2CBOpen.set(Boolean.FALSE);

                }else if(lookup.equals("rest1lookup")){
                    CBImplementor.rest1CBOpen.set(Boolean.FALSE);
                }
                doSyncTrig.decrementAndGet();
            }

            if(rest2openState == 1.0){
                isClosed = false;
                if(lookup.equals("rest2lookup")){
                    CBImplementor.rest2CBOpen.set(Boolean.TRUE);
                }else if(lookup.equals("rest1lookup")){
                    CBImplementor.rest1CBOpen.set(Boolean.TRUE);
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
