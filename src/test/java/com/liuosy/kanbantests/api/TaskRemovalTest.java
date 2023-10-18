package com.liuosy.kanbantests.api;

import com.liuosy.kanbantests.common.BaseApiTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.http.Method;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TaskRemovalTest extends BaseApiTest {

    private int projectId;
    private int taskId;

    private String taskName = String.format("LS Test Task_%s", tmst);

    @BeforeClass(alwaysRun = true)
    public void userAndProjectCreation() {

        userId = createUser();
        projectId = createProject();
        taskId = createTask(taskName, projectId);
        System.out.println(String.format("Preconditions: \nNew user ID is: %d. \nNew project ID is: %d \nNew task ID is: %d\n--------", userId, projectId, taskId));

    }



    @Feature("API Testing")
    @Story("Task Removal")
    @Test(priority = 0, groups = "APITests")
    @Description("Delete Task - Happy Pass")
    public void deleteTaskHappyPassTest() {

        String removeTaskRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeTask", String.format("{\"task_id\": \"%d\"}", taskId));

        Response removeTaskResponse = sendRequest(Method.DELETE, removeTaskRequestBodyJson, 200);

        boolean isTaskRemoved = removeTaskResponse.jsonPath().getBoolean("result");
        Assert.assertTrue(isTaskRemoved);
        System.out.println(String.format("Test Results: \nThe task id = %d is removed: %b", taskId, isTaskRemoved));

    }


    @Feature("API Testing")
    @Story("Task Removal")
    @Test(priority = 1)
    @Description("Delete Task That Is Already Deleted")
    public void deleteNonExistingTaskTest() {

        String removeTaskRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeTask", String.format("{\"task_id\": \"%d\"}", taskId));

        Response removeTaskResponse = sendRequest(Method.DELETE, removeTaskRequestBodyJson, 200);

        boolean isTaskRemoved = removeTaskResponse.jsonPath().getBoolean("result");
        Assert.assertFalse(isTaskRemoved);
        System.out.println(String.format("The non-existing task is removed: %b", isTaskRemoved));

    }


    @Feature("API Testing")
    @Story("Task Removal")
    @Test(priority = 1)
    @Description("Delete Task Without Providing Task ID")
    public void deleteTaskWithEmptyIdTest() {

        String removeTaskRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeTask", "{\"task_id\": \"\"}");

        Response removeTaskResponse = sendRequest(Method.DELETE, removeTaskRequestBodyJson, 200);

        boolean isTaskRemoved = removeTaskResponse.jsonPath().getBoolean("result");
        Assert.assertFalse(isTaskRemoved);
        System.out.println(String.format("The task with no ID is removed: %b", isTaskRemoved));

    }

    @AfterClass(alwaysRun = true)
    public void projectAndUserRemovalApiTest() {

        boolean isProjectRemoved = deleteProject(projectId);
        boolean isUserRemoved = deleteUser();
        System.out.println(String.format("Postcondition: \nThe project is removed: %b. \n The user is removed: %b", isProjectRemoved, isUserRemoved));

    }

}
