package com.liuosy.kanbantests.common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.sql.Timestamp;

public class BaseTest {
    public final static String REQUEST_JSON_TEMPLATE = "{\"jsonrpc\": \"2.0\", \"method\": \"%s\", \"id\": 1518863034, \"params\": %s}";
    public final static String URL = "http://localhost:80/jsonrpc.php";
    public final static String API_USER_NAME = "jsonrpc";
    public final static String API_TOKEN = "54c1ce721a0d09e27bec84a242f54df97da9bc9245ef7424fd335a6e9c6e";


    protected Timestamp tmst = new Timestamp(System.currentTimeMillis());
    protected String newUserName = String.format("LS_Test_User_%s", tmst);
    protected String newUserPassword = "LS_123456!";
    protected String projectName = String.format("LS Test Project_%s", tmst);
    protected int userId;




    protected Response sendRequest(Method httpMethod, String requestBodyJson, int expectedStatusCode) {
        return RestAssured.given()
            .auth().preemptive().basic(API_USER_NAME, API_TOKEN)
            .contentType(ContentType.JSON)
            .body(requestBodyJson)
            .when()
            .request(httpMethod, URL)
            .then()
            .assertThat()
            .statusCode(expectedStatusCode)
            .extract().response();
    }

    protected int createUser(){
        String createUserRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createUser", String.format("{\"username\": \"%s\", \"password\": \"%s\"}", newUserName, newUserPassword));

        Response createUserResponse = sendRequest(Method.POST, createUserRequestBodyJson, 200);
        return createUserResponse.jsonPath().getInt("result");
    }

    protected int createProject(){
        String createProjectRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createProject", String.format("{\"name\": \"%s\", \"owner_id\": \"%d\"}", projectName, userId));

        Response createProjectResponse = sendRequest(Method.POST, createProjectRequestBodyJson, 200);
        return createProjectResponse.jsonPath().getInt("result");
    }

    protected boolean deleteUser(){
        String deleteUserRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeUser", String.format("{\"user_id\": \"%d\"}", userId));

        Response response = sendRequest(Method.DELETE, deleteUserRequestBodyJson, 200);
        return response.jsonPath().getBoolean("result");
    }

    protected boolean deleteProject(int projectId){
        String removeProjectRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeProject", String.format("{\"project_id\": \"%d\"}", projectId));

        Response removeProjectResponse = sendRequest(Method.DELETE, removeProjectRequestBodyJson, 200);
        return removeProjectResponse.jsonPath().getBoolean("result");

    }

}
