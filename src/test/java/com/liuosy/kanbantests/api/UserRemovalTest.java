package com.liuosy.kanbantests.api;

import com.liuosy.kanbantests.common.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UserRemovalTest extends BaseApiTest {

    @BeforeClass(alwaysRun = true)
    public void userCreation(){
        userId = createUser();
        System.out.printf("New user ID is: %d%n", userId);
    }

    //Delete Existing User
    @Test(priority = 0, groups = "APITests")
    public void userRemovalApiTest(){

        SoftAssert softAssert = new SoftAssert();
        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeUser", String.format("{\"user_id\": \"%d\"}", userId));

        Response response = sendRequest(Method.DELETE, requestBodyJson, 200);

        boolean isRemoved = response.jsonPath().getBoolean("result");
        softAssert.assertTrue(isRemoved);
        Response getUserDataResponse = getUserData(userId);
        softAssert.assertNull(getUserDataResponse.jsonPath().getString("result"));
        softAssert.assertAll();
        System.out.printf("The user with UserID = %d is removed: %b%n", userId, isRemoved);
    }

    //Delete Non-Existing User
    @Test(priority = 1)
    public void nonExistingUserRemovalTest(){

        String requestBodyJson = String.format(REQUEST_JSON_TEMPLATE,
                "removeUser", String.format("{\"user_id\": \"%d\"}", userId));

        Response response = sendRequest(Method.DELETE, requestBodyJson, 200);

        boolean isRemoved = response.jsonPath().getBoolean("result");
        Assert.assertFalse(isRemoved);
        System.out.printf("The non-existing user is removed? %b%n", isRemoved);

    }

}
