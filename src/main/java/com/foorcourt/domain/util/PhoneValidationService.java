package com.foorcourt.domain.util;

import org.springframework.stereotype.Component;

import static com.foorcourt.domain.util.Const.PATTERN_PHONE;

@Component
public class PhoneValidationService {
    // El patrón definido requiere: entre 6 a 13 numeros
    public boolean isValidPhone(String phone) {
        return phone != null && PATTERN_PHONE.matcher(phone).matches();
    }
}
