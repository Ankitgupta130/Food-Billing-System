package com.avion.billing_system.service;


import com.avion.billing_system.entity.Snack;
import com.avion.billing_system.repository.SnackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnackService {

    @Autowired
    private SnackRepository snackRepository;

    public Snack saveSnack(Snack snack){
        return snackRepository.save(snack);
    }

    public List<Snack> getAllSnacks() {
        return snackRepository.findAll();
    }

    public Optional<Snack> getSnackById(Long id) {
        return snackRepository.findById(id);
    }

    public void deleteSnack(Long id){
        snackRepository.deleteById(id);
    }
}
