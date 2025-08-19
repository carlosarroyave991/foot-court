package com.foorcourt.domain.util;

import org.springframework.stereotype.Component;

import static com.foorcourt.domain.util.Const.PATTERN_NIT;


@Component
public class NitValidationService {
    // El patrón definido requiere: 9 digitos
    public boolean isValidNit(String nit) {
        return nit != null && PATTERN_NIT.matcher(nit).matches();
    }
}
