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

public class ProjectCreationTest extends BaseApiTest {

    private int projectId;

    @BeforeClass(alwaysRun = true)
    public void userCreation(){

        userId = createUser();
        System.out.println(String.format("Preconditions: \nNew user ID is: %d \n--------", userId));

    }

    //Create New Project - Happy pass
    @Feature("API Testing")
    @Story("Project Creation")
    @Test(priority = 0, groups = "APITests")
    public void projectCreationHappyPassTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createProject", String.format("{\"name\": \"%s\", \"owner_id\": \"%d\"}",projectName, userId));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        projectId = response.jsonPath().getInt("result");
        Assert.assertTrue(projectId > 0);
        System.out.println(String.format("Test Results: \nNew project with ID = %d is created.", projectId));

    }

    //Create Duplicated Project - No new project should be created

    @Feature("API Testing")
    @Story("Project Creation")
    @Test(priority = 1)
    public void duplicatedProjectCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createProject", String.format("{\"name\": \"%s\", \"owner_id\": \"%d\"}",projectName, userId));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is duplicated project created?  %b", isCreated));

    }

    //Create Project With No Name - No new project should be created

    @Feature("API Testing")
    @Story("Project Creation")
    @Test(priority = 2)
    public void emptyNameProjectCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createProject", "{\"name\": \"\"}");

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.println(String.format("Is new project without title created?  %b", isCreated));

    }

    @AfterClass(alwaysRun = true)
    public void projectAndUserRemoval(){

        boolean isProjectRemoved = deleteProject(projectId);
        boolean isUserRemoved = deleteUser();
        System.out.println(String.format("Postcondition: \nThe project is removed: %b. \n The user is removed: %b", isProjectRemoved, isUserRemoved));

    }

}
