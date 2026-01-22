package com.avion.billing_system.service;


import com.avion.billing_system.dto.RecaptchaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class RecaptchaService {
    //
//    @Value("${recaptcha.secret}")
    private String secretKey="6Lcm-aQrAAAAANcVNeQSaN9gEGSWnNP6Oc2TjFti";

    private static final String GOOGLE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String token) {
        RestTemplate restTemplate = new RestTemplate();
        log.info("captcha in controller");
        Map<String, String> params = new HashMap<>();
        params.put("secret", secretKey);
        params.put("response", token);

        RecaptchaResponse response = restTemplate.postForObject(
                GOOGLE_VERIFY_URL + "?secret={secret}&response={response}",
                null,
                RecaptchaResponse.class,
                params
        );

        return response != null && response.isSuccess();
    }
}
