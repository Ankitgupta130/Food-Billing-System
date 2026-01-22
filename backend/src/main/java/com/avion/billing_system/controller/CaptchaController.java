package com.avion.billing_system.controller;

import com.avion.billing_system.service.RecaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CaptchaController {

    @Autowired
    private RecaptchaService recaptchaService;

    @PostMapping("/verify-captcha")
    public Map<String, Object> verifyCaptcha(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        log.info("captcha in controller");
        boolean isValid = recaptchaService.verify(token);

        return Map.of("success", isValid);
    }
}