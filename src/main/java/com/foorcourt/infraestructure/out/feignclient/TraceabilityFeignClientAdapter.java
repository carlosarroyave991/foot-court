package com.foorcourt.infraestructure.out.feignclient;

import com.foorcourt.domain.model.feignclient.TraceabilityModel;
import com.foorcourt.domain.spi.ITraceabilityFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TraceabilityFeignClientAdapter implements ITraceabilityFeignClientPort {
    
    private final TraceabilityFeignClient traceabilityFeignClient;
    
    @Override
    public void saveTraceability(TraceabilityModel traceability) {
        traceabilityFeignClient.saveTraceability(traceability);
    }

    @Override
    public List<TraceabilityModel> getTraceabilityByClientId(Long clientId) {
        return traceabilityFeignClient.getTraceabilityByClientId(clientId);
    }
}