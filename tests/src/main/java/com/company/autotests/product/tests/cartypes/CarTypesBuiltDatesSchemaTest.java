package com.company.autotests.product.tests.cartypes;

import java.util.Map;
import java.util.stream.Stream;

import com.company.autotests.product.clients.CarTypesClient;
import com.company.autotests.product.clients.Paths;
import com.company.autotests.product.tests.Requirements;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Story(Requirements.Story.SCHEMA)
@Feature(Requirements.Feature.CAR_TYPES)
@Severity(SeverityLevel.BLOCKER)
@DisplayName("CarTypesBuiltDatesSchemaTest")
class CarTypesBuiltDatesSchemaTest {

    @ParameterizedTest
    @MethodSource("builtDatesQueryParamsProvider")
    void builtDatesTest(Map<String, String> queryParams) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_BUILT_DATES)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParams(queryParams)
                .readAsResponse();
        response.then().log().ifValidationFails(LogDetail.ALL)
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/car_types_scheme.json"));
    }

    static Stream<Arguments> builtDatesQueryParamsProvider() {
        return Stream.of(
                Arguments.arguments(ImmutableMap.of()),
                Arguments.arguments(ImmutableMap.of()),
                Arguments.arguments(ImmutableMap.of()));
    }
}
