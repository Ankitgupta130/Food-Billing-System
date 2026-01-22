package com.avion.billing_system.controller;

import com.avion.billing_system.entity.Snack;
import com.avion.billing_system.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/snacks")
@RequiredArgsConstructor
public class SnackController {

    @Autowired
    private SnackService snackService;

    @PostMapping
    public ResponseEntity<Snack> createSnack(@RequestBody Snack snack) {
        Snack saved = snackService.saveSnack(snack);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Snack>> getAllSnacks() {
        return ResponseEntity.ok(snackService.getAllSnacks());
    }

    @DeleteMapping({"/id"})
    public void delete(@PathVariable Long id) {snackService.deleteSnack(id);}
}
