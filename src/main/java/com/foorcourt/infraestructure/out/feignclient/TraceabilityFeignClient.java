package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.TraceabilityModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "traceability-service", url = "http://localhost:8083")
public interface TraceabilityFeignClient {
    
    @PostMapping("/api/v1/traceability")
    void saveTraceability(@RequestBody TraceabilityModel traceability);
    
    @GetMapping("/api/v1/traceability/client/{clientId}")
    List<TraceabilityModel> getTraceabilityByClientId(@PathVariable("clientId") Long clientId);
}