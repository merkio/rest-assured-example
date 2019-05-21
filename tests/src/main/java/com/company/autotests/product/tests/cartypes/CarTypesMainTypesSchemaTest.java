package com.company.autotests.product.tests.cartypes;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.company.autotests.product.clients.CarTypesClient;
import com.company.autotests.product.clients.Paths;
import com.company.autotests.product.tests.Requirements;
import com.company.autotests.product.tests.utils.CarTypesUtils;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Story(Requirements.Story.SCHEMA)
@Feature(Requirements.Feature.CAR_TYPES)
@Severity(SeverityLevel.BLOCKER)
@DisplayName("CarTypesMainTypesSchemaTest")
class CarTypesMainTypesSchemaTest {

    private static List<String> locales;

    @BeforeAll
    static void init() {
        locales = CarTypesUtils.getLocales();
    }

    @ParameterizedTest(name = "Positive QueryParams: {0}")
    @MethodSource("mainTypesQueryParamsProvider")
    void mainTypesTest(Map<String, String> queryParams) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MAIN_TYPES)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParams(queryParams)
                .readAsResponse();
        response.then().log().ifValidationFails(LogDetail.ALL)
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/car_types_scheme.json"));
    }

    @ParameterizedTest(name = "Negative QueryParams: {0}")
    @MethodSource("mainTypesNegativeQueryParamsProvider")
    void mainTypesNegativeTest(Map<String, String> queryParams) {
        Response response = CarTypesClient.getClient()
                .path(Paths.CAR_TYPES_MAIN_TYPES)
                .queryParam("wa_key", PROPERTIES.waKey())
                .queryParams(queryParams)
                .readAsResponse();
        response.then().log().ifValidationFails(LogDetail.ALL)
                .assertThat()
                .statusCode(400);
    }

    static Stream<Arguments> mainTypesQueryParamsProvider() {
        return Stream.of(
                Arguments.arguments(ImmutableMap.of(
                        "locale", "en",
                        "manufacturer", "107"
                )),
                Arguments.arguments(ImmutableMap.of(
                        "locale", "fr",
                        "manufacturer", "935"
                )),
                Arguments.arguments(ImmutableMap.of(
                        "locale", "de",
                        "manufacturer", "570"
                ))
        );
    }

    static Stream<Arguments> mainTypesNegativeQueryParamsProvider() {
        return Stream.of(
                Arguments.arguments(ImmutableMap.of("manufacturer", "", "locale", "us")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "107", "locale", "21")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "", "locale", "")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "234", "locale", "")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "-12", "locale", "fr")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "0", "locale", "us")),
                Arguments.arguments(ImmutableMap.of("manufacturer", String.valueOf(Integer.MAX_VALUE), "locale", "us")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "$(%", "locale", "us")),
                Arguments.arguments(ImmutableMap.of("manufacturer", "935", "locale", "$)")));
    }
}
