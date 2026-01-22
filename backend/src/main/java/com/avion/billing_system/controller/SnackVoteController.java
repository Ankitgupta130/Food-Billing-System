package com.avion.billing_system.controller;

import com.avion.billing_system.service.SnackVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/snack-votes")
@RequiredArgsConstructor
public class SnackVoteController {

    @Autowired
    private SnackVoteService snackVoteService;

    @GetMapping("/count")
    public ResponseEntity<?> getSnackVoteCounts() {
        return ResponseEntity.ok(snackVoteService.getAllSnackVoteCounts());
    }

//    @PostMapping
//    public ResponseEntity<?> castVote(@RequestBody SnackVote vote){
//        return ResponseEntity.ok(snackVoteService.save)
//    }
}
