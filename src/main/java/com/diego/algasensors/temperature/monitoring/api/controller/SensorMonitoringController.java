package com.diego.algasensors.temperature.monitoring.api.controller;

import com.diego.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.diego.algasensors.temperature.monitoring.domain.model.SensorId;
import com.diego.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.diego.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors/{sensorId}/monitoring")
public class SensorMonitoringController {

    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public SensorMonitoringOutput getDetails(@PathVariable TSID sensorId) {

        SensorMonitoring sensorMonitoring = getSensorMonitoring(sensorId);

        return SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId().getValue())
                .enabled(sensorMonitoring.getEnabled())
                .lastTemperature(sensorMonitoring.getLastTemperature())
                .updatedAt(sensorMonitoring.getUpdatedAt()).build();

    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SneakyThrows
    public void enable(@PathVariable TSID sensorId) {

        SensorMonitoring sensorMonitoringOutput = getSensorMonitoring(sensorId);

        if(sensorMonitoringOutput.getEnabled())
            Thread.sleep(10000);

        sensorMonitoringOutput.setEnabled(true);

        sensorMonitoringRepository.saveAndFlush(sensorMonitoringOutput);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoringOutput = getSensorMonitoring(sensorId);
        sensorMonitoringOutput.setEnabled(false);

        sensorMonitoringRepository.saveAndFlush(sensorMonitoringOutput);
    }

    private SensorMonitoring getSensorMonitoring(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updatedAt(null).
                        build());
    }

}
