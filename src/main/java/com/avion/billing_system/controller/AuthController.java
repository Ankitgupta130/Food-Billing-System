package com.avion.billing_system.controller;


import com.avion.billing_system.dto.ActiveDeactiveReq;
import com.avion.billing_system.dto.LoginRequest;
import com.avion.billing_system.dto.LoginResponse;
import com.avion.billing_system.dto.Userdto;
import com.avion.billing_system.entity.User;
import com.avion.billing_system.repository.UserRepository;
import com.avion.billing_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/action")
    public ResponseEntity<Userdto> setUser(@RequestBody ActiveDeactiveReq activeDeactiveReq) {
      Userdto userdto = userService.setUserStatus(activeDeactiveReq);

      return ResponseEntity.ok(userdto);

    }
}
