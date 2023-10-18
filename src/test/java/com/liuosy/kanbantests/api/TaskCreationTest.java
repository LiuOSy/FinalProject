package com.liuosy.kanbantests.api;

import com.liuosy.kanbantests.common.BaseApiTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TaskCreationTest extends BaseApiTest {
    private int projectId;
    private int taskId;

    private String taskName = String.format("LS Test Task_%s", tmst);

    @BeforeClass(alwaysRun = true)
    public void userAndProjectCreation() {

        userId = createUser();
        projectId = createProject();
        System.out.println(String.format("Preconditions: \nNew user ID is: %d. \nNew project ID is: %d\n--------", userId, projectId));

    }

    //Create New Task - Happy pass
    @Feature("API Testing")
    @Story("Task Creation")
    @Test(priority = 0, groups = "APITests")
    public void taskCreationHappyPassTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", String.format("{\"title\": \"%s\", \"project_id\": \"%d\"}", taskName, projectId));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        taskId = response.jsonPath().getInt("result");
        Assert.assertTrue(projectId > 0);
        System.out.println(String.format("Test Results: \nNew task ID %d is created in the project id %d", taskId, projectId));

    }

    //Create Duplicated Task - No new task should be created
    @Feature("API Testing")
    @Story("Task Creation")
    @Test(priority = 1)
    public void duplicateTaskCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", String.format("{\"title\": \"%s\", \"project_id\": \"%d\"}", taskName, projectId));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is duplicated task created?  %b", isCreated));
    }

    //Create New Task Without Title and Project - No Task should be created
    @Feature("API Testing")
    @Story("Task Creation")
    @Test(priority = 2)
    public void emptyTitleAndProjectTaskCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", "{\"title\": \"\", \"project_id\": \"\"}");

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is new task without title and project created?  %b", isCreated));

    }

    //Create New Task Without Project - No Task should be created
    @Feature("API Testing")
    @Story("Task Creation")
    @Test(priority = 3)
    public void emptyProjectTaskCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", String.format("{\"title\": \"%s\", \"project_id\": \"\"}", taskName));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is new task without project created?  %b", isCreated));

    }

    //Create New Task Without Title - No Task should be created
    @Feature("API Testing")
    @Story("Task Creation")
    @Test(priority = 4)
    public void emptyTitleTaskCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createTask", String.format("{\"title\": \"\", \"project_id\": \"%d\"}", projectId));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is new task without title created?  %b", isCreated));

    }

    @AfterClass(alwaysRun = true)
    public void taskProjectAndUserRemoval() {
        boolean isTaskRemoved = deleteTask(taskId);
        boolean isProjectRemoved = deleteProject(projectId);
        boolean isUserRemoved = deleteUser();
        System.out.println(String.format("Postcondition: \nThe task with Id = %d is removed: %b. \nThe project is removed: %b. \n The user is removed: %b", taskId, isTaskRemoved, isProjectRemoved, isUserRemoved));

    }


}
