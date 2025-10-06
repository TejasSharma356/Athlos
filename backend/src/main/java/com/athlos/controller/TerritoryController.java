package com.athlos.controller;

import com.athlos.entity.Territory;
import com.athlos.repository.TerritoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/territories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class TerritoryController {

    @Autowired
    private TerritoryRepository territoryRepository;

    @GetMapping("/active")
    public ResponseEntity<List<Territory>> getActiveTerritories() {
        List<Territory> list = territoryRepository.findByIsActiveTrue();
        return ResponseEntity.ok(list);
    }
}


