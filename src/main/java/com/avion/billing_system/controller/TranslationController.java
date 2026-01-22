package com.avion.billing_system.controller;

import com.avion.billing_system.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/i18n")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getMessages(@RequestParam(defaultValue = "en") String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        return translationService.getAllMessages(locale);
    }


}
