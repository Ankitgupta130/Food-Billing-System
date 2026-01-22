package com.avion.billing_system.service;

import com.avion.billing_system.repository.SnackVoteRepository;
import com.avion.billing_system.repository.projection.SnackVoteCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnackVoteService {
    private final SnackVoteRepository snackVoteRepository;

    public List<SnackVoteCount> getAllSnackVoteCounts() {
        return snackVoteRepository.countVotesPerSnack();
    }
}
