package com.avion.billing_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TranslationService {

    @Autowired
    private MessageSource messageSource;

    public Map<String, String> getAllMessages(Locale locale) {
        Map<String, String> messages = new HashMap<>();

        ResourceBundle bundle = ResourceBundle.getBundle("i18n/messages", locale);

        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = messageSource.getMessage(key, null, locale);
            messages.put(key, value);
        }
        return messages ;
    }


}
