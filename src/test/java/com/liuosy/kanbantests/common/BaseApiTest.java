package com.liuosy.kanbantests.common;

import io.restassured.http.Method;
import io.restassured.response.Response;

public class BaseApiTest extends BaseTest {

    protected Response getUserData(int userId){
        String getUserDataRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "getUser", String.format("{\"user_id\": %d}", userId));

        return sendRequest(Method.GET, getUserDataRequestBodyJson, 200);

    }

    protected int createTask(String taskName, int projectId){
        String createTaskRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", String.format("{\"title\": \"%s\", \"project_id\": \"%d\"}", taskName, projectId));

        Response response = sendRequest(Method.POST, createTaskRequestBodyJson, 200);
        return response.jsonPath().getInt("result");
    }

    protected boolean deleteTask(int taskId){
        String removeTaskRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeTask", String.format("{\"task_id\": \"%d\"}", taskId));

        Response removeTaskResponse = sendRequest(Method.DELETE, removeTaskRequestBodyJson, 200);
        return removeTaskResponse.jsonPath().getBoolean("result");

    }

}
