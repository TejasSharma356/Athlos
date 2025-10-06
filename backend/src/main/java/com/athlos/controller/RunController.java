package com.athlos.controller;

import com.athlos.dto.RunDTO;
import com.athlos.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/runs")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class RunController {
    
    @Autowired
    private RunService runService;
    
    @PostMapping("/start")
    public ResponseEntity<RunDTO> startRun(@RequestBody StartRunRequest request) {
        try {
            RunDTO run = runService.startRun(request.getUserId());
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{runId}/pause")
    public ResponseEntity<RunDTO> pauseRun(@PathVariable Long runId) {
        try {
            RunDTO run = runService.pauseRun(runId);
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{runId}/resume")
    public ResponseEntity<RunDTO> resumeRun(@PathVariable Long runId) {
        try {
            RunDTO run = runService.resumeRun(runId);
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{runId}/end")
    public ResponseEntity<RunDTO> endRun(@PathVariable Long runId) {
        try {
            RunDTO run = runService.endRun(runId);
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{runId}/point")
    public ResponseEntity<RunDTO> addRunPoint(@PathVariable Long runId, @RequestBody RunPointRequest request) {
        try {
            RunDTO run = runService.addRunPoint(runId, request.getLatitude(), request.getLongitude(), request.getStepCount());
            return ResponseEntity.ok(run);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RunDTO>> getUserRuns(@PathVariable Long userId) {
        try {
            List<RunDTO> runs = runService.getUserRuns(userId);
            return ResponseEntity.ok(runs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<RunDTO> getActiveRun(@PathVariable Long userId) {
        try {
            Optional<RunDTO> run = runService.getActiveRun(userId);
            return run.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Request DTOs
    public static class StartRunRequest {
        private Long userId;
        
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
    
    public static class RunPointRequest {
        private Double latitude;
        private Double longitude;
        private Integer stepCount;
        
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        public Integer getStepCount() { return stepCount; }
        public void setStepCount(Integer stepCount) { this.stepCount = stepCount; }
    }
}

