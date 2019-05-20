package com.company.autotests.product.tests.cartypes;

import com.company.autotests.product.tests.Requirements;
import com.company.autotests.product.clients.CarTypesClient;
import com.company.autotests.product.clients.Paths;
import com.company.autotests.product.rules.LoggingRule;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@ExtendWith(LoggingRule.class)
@Story(Requirements.Story.SCHEMA)
@Feature(Requirements.Feature.CAR_TYPES)
@Severity(SeverityLevel.BLOCKER)
class CarTypesMainTypesSchemaTest {

    @Test
    @ParameterizedTest
    @MethodSource("mainTypesQueryParamsProvider")
    void mainTypesTest(Map<String, String> queryParams) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MAIN_TYPES)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParams(queryParams)
                .readAsResponse();
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemes/car_types_scheme.json"));
    }

    static Stream<Arguments> mainTypesQueryParamsProvider() {
        return Stream.of(
                Arguments.arguments(ImmutableMap.of()),
                Arguments.arguments(ImmutableMap.of()),
                Arguments.arguments(ImmutableMap.of()));
    }
}
