package com.liuosy.kanbantests.api;
import com.liuosy.kanbantests.common.BaseApiTest;
import io.restassured.http.Method;
import org.testng.Assert;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.LinkedHashMap;

public class UserCreationTest extends BaseApiTest {

    //Create New User - Happy pass
    @Test (priority = 0, groups = "APITests")
    public void userCreationHappyPassTest(){

        SoftAssert softAssert = new SoftAssert();

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createUser", String.format("{\"username\": \"%s\", \"password\": \"%s\"}", newUserName, newUserPassword));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        userId = response.jsonPath().getInt("result");
        softAssert.assertTrue(userId > 0);
        System.out.println(String.format("New user ID is: %d", userId));

        Response getUserDataResponse = getUserData(userId);
        getUserDataResponse.jsonPath().getJsonObject("result");

        LinkedHashMap userData = (LinkedHashMap)getUserDataResponse.jsonPath().getJsonObject("result");

        softAssert.assertEquals(userData.get("id"), userId);
        softAssert.assertEquals(userData.get("username"), newUserName);
        softAssert.assertAll();

    }

    //Create Duplicated User - No new user should be created
    @Test (priority = 1)
    public void duplicatedUserCreationTest(){

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createUser", String.format("{\"username\": \"%s\", \"password\": \"%s\"}", newUserName, newUserPassword));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.printf("Is duplicated user created?  %b%n", isCreated);

    }

    //Create User With Empty Password - No new user should be created
    @Test (priority = 2)
    public void emptyPasswordUserCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createUser", String.format("{\"username\": \"%s\", \"password\": \"\"}", newUserName));

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.printf("Is user without password created?  %b%n", isCreated);
    }

    //Create User With Empty Username and Password - No new user should be created
    @Test (priority = 3)
    public void emptyUsernameAndPasswordUserCreationTest() {

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "createUser", "{\"username\": \"\", \"password\": \"\"}");

        Response response = sendRequest(Method.POST, requestBodyJson, 200);

        boolean isCreated = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isCreated);
        System.out.printf("Is user without username and password created?  %b%n", isCreated);
    }

    @AfterClass(alwaysRun = true)
    public void userRemoval(){

        boolean isRemoved = deleteUser();
        System.out.printf("The user is removed: %b%n", isRemoved);

    }

}
