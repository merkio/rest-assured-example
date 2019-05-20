package com.company.autotests.product.tests.cartypes;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import com.company.autotests.product.clients.CarTypesClient;
import com.company.autotests.product.clients.Paths;
import com.company.autotests.product.tests.Requirements;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Story(Requirements.Story.SCHEMA)
@Feature(Requirements.Feature.CAR_TYPES)
@Severity(SeverityLevel.BLOCKER)
@DisplayName("CarTypesManufactureSchemaTest")
class CarTypesManufactureSchemaTest {

    @ParameterizedTest(name = "Locale: {0}")
    @MethodSource("manufactureLocalesProvider")
    void manufactureTest(String locale) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MANUFACTURER)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParam("locale", locale)
                .readAsResponse();
        response.then().log().ifValidationFails(LogDetail.ALL)
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/car_types_scheme.json"));
    }

    @ParameterizedTest(name = "Negative Locale: {0}")
    @NullAndEmptySource
    @ValueSource(strings = {"", "=", "1th1", "23", "^(", ""})
    void manufactureNegativeTest(String locale) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MANUFACTURER)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParam("locale", locale)
                .readAsResponse();
        response.then().log().ifValidationFails(LogDetail.ALL)
                .assertThat()
                .statusCode(400);
    }

    static Stream<Arguments> manufactureLocalesProvider() {
        Locale[] locales = Locale.getAvailableLocales();
        return Arrays.stream(locales)
                .filter(locale -> !locale.getLanguage().isEmpty())
                .map(Locale::getLanguage)
                .distinct()
                .map(Arguments::of);
    }

}
