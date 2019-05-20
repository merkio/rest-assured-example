package com.company.autotests.product.clients;

import io.restassured.response.Response;
import org.apache.http.client.utils.URIBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.company.autotests.product.properties.TestProperties.PROPERTIES;
import static io.restassured.RestAssured.given;

public class CarTypesClient {

    private URIBuilder uriBuilder;
    private Map<String, String> queryParams = new HashMap<>();

    private CarTypesClient(URI host) {
        this.uriBuilder = new URIBuilder(host);
    }

    public static CarTypesClient getClient() {
        return new CarTypesClient(PROPERTIES.host());
    }

    public CarTypesClient path(String path) {
        this.uriBuilder.setPath(path);
        return this;
    }

    public CarTypesClient queryParam(String name, Object value) {
        this.queryParams.put(name, String.valueOf(value));
        return this;
    }

    public CarTypesClient queryParams(Map<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    public Response readAsResponse() {
        queryParams.forEach((key, value) -> this.uriBuilder.addParameter(key, value));
        try {
            return given().get(uriBuilder.build());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T read(Class<T> clazz) {
        return readAsResponse().as(clazz);
    }
}
