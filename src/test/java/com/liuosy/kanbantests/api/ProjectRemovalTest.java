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

public class ProjectRemovalTest extends BaseApiTest {
    private int projectId;

    @BeforeClass(alwaysRun = true)
    public void userAndProjectCreation() {

        userId = createUser();
        projectId = createProject();
        System.out.println(String.format("Preconditions: \nNew user ID is: %d. \nNew project ID is: %d\n--------", userId, projectId));

    }


    //Delete Project - Happy Pass
    @Feature("API Testing")
    @Story("Project Removal")
    @Test(priority = 0, groups = "APITests")
    public void deleteProjectHappyPassTest() {

        String removeProjectRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeProject", String.format("{\"project_id\": \"%d\"}", projectId));

        Response removeProjectResponse = sendRequest(Method.DELETE, removeProjectRequestBodyJson, 200);

        boolean isProjectRemoved = removeProjectResponse.jsonPath().getBoolean("result");
        Assert.assertTrue(isProjectRemoved);
        System.out.println(String.format("Test Results: \nThe project id = %d is removed: %b", projectId, isProjectRemoved));

    }

    //Delete Non-Existing Project
    @Feature("API Testing")
    @Story("Project Removal")
    @Test(priority = 1)
    public void deleteNonExistingProjectTest() {

        String removeProjectRequestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeProject", String.format("{\"project_id\": \"%d\"}", projectId));

        Response removeProjectResponse = sendRequest(Method.DELETE, removeProjectRequestBodyJson, 200);

        boolean isProjectRemoved = removeProjectResponse.jsonPath().getBoolean("result");
        Assert.assertFalse(isProjectRemoved);
        System.out.println(String.format("The non-existing project is removed: %b", isProjectRemoved));

    }


    @AfterClass(alwaysRun = true)
    public void projectAndUserRemovalApiTest() {

        boolean isUserRemoved = deleteUser();
        System.out.println(String.format("Postcondition: \nThe user with ID %d is removed: %b", userId, isUserRemoved));

    }
}
