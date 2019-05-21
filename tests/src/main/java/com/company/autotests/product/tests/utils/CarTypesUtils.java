package com.company.autotests.product.tests.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.company.autotests.product.clients.CarTypesClient;
import com.company.autotests.product.clients.Paths;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;

/**
 * @author imerk
 */
public class CarTypesUtils {

    public static List<String> getLocales() {
        return Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> !locale.getLanguage().isEmpty())
                .map(Locale::getLanguage)
                .distinct().collect(Collectors.toList());
    }

    public static List<String> getManufacturerCodes(String locale) {
        return CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MANUFACTURER)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParam("locale", locale)
                .readAsResponse().jsonPath().getMap("wkda").keySet().stream().map(String::valueOf)
                .distinct()
                .collect(Collectors.toList());
    }

    public static List<String> getMainTypeCodes(String locale, String manufacturerCode) {
        return CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MAIN_TYPES)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParam("locale", locale)
                .queryParam("manufacturer", manufacturerCode)
                .readAsResponse().jsonPath().getMap("wkda").keySet().stream().map(String::valueOf)
                .distinct()
                .collect(Collectors.toList());
    }
}
