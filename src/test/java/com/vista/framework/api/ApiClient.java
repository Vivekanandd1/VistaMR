package com.vista.framework.api;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

/**
 * API Client for REST API operations.
 * Provides fluent interface for API testing with built-in logging and validation.
 */
public class ApiClient {
    
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static ApiClient instance;
    private String baseUrl;
    private Map<String, String> defaultHeaders;
    private ResponseSpecification defaultResponseSpec;
    
    private ApiClient() {
        this.baseUrl = config.get(ConfigKeys.API_BASE_URL, 
                        config.get(ConfigKeys.BASE_URL, "https://vista.kreditz-dev.com"));
        this.defaultHeaders = new HashMap<>();
        initializeDefaultResponseSpec();
    }
    
    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }
    
    private void initializeDefaultResponseSpec() {
        defaultResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(30000L))
                .log(LogDetail.ALL)
                .build();
    }
    
    /**
     * Set base URL for API calls
     */
    public ApiClient setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        logger.info("API Base URL set to: {}", baseUrl);
        return this;
    }
    
    /**
     * Set default header for all API calls
     */
    public ApiClient setDefaultHeader(String key, String value) {
        defaultHeaders.put(key, value);
        logger.debug("Default header set: {} = {}", key, value);
        return this;
    }
    
    /**
     * Set authorization token
     */
    public ApiClient setAuthToken(String token) {
        defaultHeaders.put("Authorization", "Bearer " + token);
        logger.debug("Auth token set");
        return this;
    }
    
    /**
     * Set API key
     */
    public ApiClient setApiKey(String apiKey) {
        defaultHeaders.put("X-API-Key", apiKey);
        logger.debug("API key set");
        return this;
    }
    
    /**
     * Clear default headers
     */
    public ApiClient clearHeaders() {
        defaultHeaders.clear();
        logger.debug("Default headers cleared");
        return this;
    }
    
    /**
     * Build request specification with default settings
     */
    private RequestSpecification buildRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        
        for (Map.Entry<String, String> header : defaultHeaders.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        
        return builder.build();
    }
    
    /**
     * GET request
     */
    @Step("GET request to {path}")
    public Response get(String path) {
        logger.info("GET request to: {}", path);
        return buildRequestSpec()
                .when()
                .get(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * GET request with query parameters
     */
    @Step("GET request to {path} with params {params}")
    public Response get(String path, Map<String, Object> params) {
        logger.info("GET request to: {} with params: {}", path, params);
        return buildRequestSpec()
                .queryParams(params)
                .when()
                .get(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * GET request with path parameter
     */
    @Step("GET request to {path} with path param {pathParam}")
    public Response get(String path, String pathParam) {
        logger.info("GET request to: {} with path param: {}", path, pathParam);
        return buildRequestSpec()
                .when()
                .get(path, pathParam)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with body
     */
    @Step("POST request to {path}")
    public Response post(String path, Object body) {
        logger.info("POST request to: {} with body: {}", path, body);
        return buildRequestSpec()
                .body(body)
                .when()
                .post(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with JSON body
     */
    @Step("POST request to {path} with JSON body")
    public Response post(String path, String jsonBody) {
        logger.info("POST request to: {} with JSON body", path);
        return buildRequestSpec()
                .body(jsonBody)
                .when()
                .post(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with form data
     */
    @Step("POST request to {path} with form data")
    public Response postForm(String path, Map<String, Object> formData) {
        logger.info("POST request to: {} with form data: {}", path, formData);
        return buildRequestSpec()
                .formParams(formData)
                .when()
                .post(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with multipart data
     */
    @Step("POST request to {path} with multipart data")
    public Response postMultipart(String path, Map<String, Object> multipartData) {
        logger.info("POST request to: {} with multipart data", path);
        return buildRequestSpec()
                .multiPart("file", multipartData)
                .when()
                .post(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * PUT request with body
     */
    @Step("PUT request to {path}")
    public Response put(String path, Object body) {
        logger.info("PUT request to: {} with body: {}", path, body);
        return buildRequestSpec()
                .body(body)
                .when()
                .put(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * PATCH request with body
     */
    @Step("PATCH request to {path}")
    public Response patch(String path, Object body) {
        logger.info("PATCH request to: {} with body: {}", path, body);
        return buildRequestSpec()
                .body(body)
                .when()
                .patch(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * DELETE request
     */
    @Step("DELETE request to {path}")
    public Response delete(String path) {
        logger.info("DELETE request to: {}", path);
        return buildRequestSpec()
                .when()
                .delete(path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * DELETE request with path parameter
     */
    @Step("DELETE request to {path} with path param {pathParam}")
    public Response delete(String path, String pathParam) {
        logger.info("DELETE request to: {} with path param: {}", path, pathParam);
        return buildRequestSpec()
                .when()
                .delete(path, pathParam)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * Custom request with specific method
     */
    @Step("{method} request to {path}")
    public Response request(String method, String path) {
        logger.info("{} request to: {}", method, path);
        return buildRequestSpec()
                .when()
                .request(method, path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * Custom request with body
     */
    @Step("{method} request to {path}")
    public Response request(String method, String path, Object body) {
        logger.info("{} request to: {} with body", method, path);
        return buildRequestSpec()
                .body(body)
                .when()
                .request(method, path)
                .then()
                .spec(defaultResponseSpec)
                .extract()
                .response();
    }
    
    /**
     * Set custom response specification
     */
    public ApiClient setResponseSpec(ResponseSpecification responseSpec) {
        this.defaultResponseSpec = responseSpec;
        logger.debug("Custom response specification set");
        return this;
    }
    
    /**
     * Expect specific status code
     */
    public ApiClient expectStatusCode(int statusCode) {
        defaultResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectResponseTime(lessThan(30000L))
                .log(LogDetail.ALL)
                .build();
        logger.debug("Expected status code set to: {}", statusCode);
        return this;
    }
    
    /**
     * Expect specific response time in milliseconds
     */
    public ApiClient expectResponseTime(long millis) {
        defaultResponseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(millis))
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .build();
        logger.debug("Expected response time set to: {}ms", millis);
        return this;
    }
    
    /**
     * Get OAuth2 access token
     */
    @Step("Getting OAuth2 token")
    public String getOAuth2Token(String clientId, String clientSecret, String tokenEndpoint) {
        logger.info("Requesting OAuth2 token from: {}", tokenEndpoint);
        
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", "client_credentials")
                .when()
                .post(tokenEndpoint)
                .then()
                .log().all()
                .extract()
                .response();
        
        String token = response.jsonPath().getString("access_token");
        logger.info("OAuth2 token obtained successfully");
        
        return token;
    }
}
