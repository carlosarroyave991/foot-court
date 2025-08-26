package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.feignclient.TraceabilityModel;

import java.util.List;

public interface ITraceabilityFeignClientPort {
    void saveTraceability(TraceabilityModel traceability);
    List<TraceabilityModel> getTraceabilityByClientId(Long clientId);
}