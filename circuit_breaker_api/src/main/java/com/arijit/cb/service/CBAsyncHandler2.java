package com.arijit.cb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CBAsyncHandler2 {

    public static final String R4J_STATE_METRIC = "resilience4j.circuitbreaker.state";

    @Autowired
    private MetricsEndpoint metricsEndpoint;


    @Async
    public synchronized void evaluateRest2CBstate(){

        List<String> rest2closedTags = new ArrayList<>();
        rest2closedTags.add("name:rest2lookup");
        rest2closedTags.add("state:closed");

        List<String> rest2halfopenTags = new ArrayList<>();
        rest2halfopenTags.add("name:rest2lookup");
        rest2halfopenTags.add("state:half_open");

        List<String> rest2openTags = new ArrayList<>();
        rest2openTags.add("name:rest2lookup");
        rest2openTags.add("state:open");


        while(true) {
            log.info("***************** PRINTING REST2 *****************");

            Double rest2closedState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2closedTags).getMeasurements().get(0).getValue();
            Double rest2halfopenState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2halfopenTags).getMeasurements().get(0).getValue();
            Double rest2openState = metricsEndpoint.metric(R4J_STATE_METRIC,rest2openTags).getMeasurements().get(0).getValue();

            log.info("-----rest2closedState:::{}------", rest2closedState);
            log.info("-----rest2halfopenState:::{}------", rest2halfopenState);
            log.info("-----rest2openState:::{}------", rest2openState);


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
