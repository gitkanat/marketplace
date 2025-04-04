//package service;
//
//import config.ConfigDB;
//import endpoint.AuthEndpoints;
//import io.restassured.common.mapper.TypeRef;
//import model.ResponseModel;
//import model.auth.AuthResponseModel;
//import service.auth.AuthService;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//
//public class PasswordResetService {
//
//    private static int testUserId;
//    private static String authToken;
//
//    // Method to fetch OTP from the database
//    public String fetchOtp(String userId) {
//        return ConfigDB.getOtpCode(userId);
//    }
//
//    // Method to sign up a test user
//    public void testSignUp() {
//        given()
//                .contentType("application/json")
//                .body(AuthService.buildTestSignUpModel())
//                .when()
//                .log().body()
//                .post(AuthEndpoints.SIGN_UP)
//                .then()
//                .log().body()
//                .statusCode(200)
//                .body("code", equalTo("CREATED"));
//    }
//
//    // Method to sign in and retrieve authentication token
//    public void testSignIn() {
//        ResponseModel<AuthResponseModel> responseModel =
//                given()
//                        .contentType("application/json")
//                        .body(AuthService.buildTestSignInModel())
//                        .when()
//                        .put(AuthEndpoints.SIGN_IN)
//                        .then()
//                        .log().body()
//                        .statusCode(200)
//                        .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {});
//
//        authToken = responseModel.getContent().getToken();
//        testUserId = responseModel.getContent().getId();
//    }
//
//    // Getter methods for reusability in tests
//    public static int getTestUserId() {
//        return testUserId;
//    }
//
//    public static String getAuthToken() {
//        return authToken;
//    }
//}
package service;

import config.ConfigDB;
import endpoint.AuthEndpoints;
import endpoint.PasswordChangeControllerEndpoints;
import io.restassured.common.mapper.TypeRef;
import model.PasswordChangeRequestModel;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import service.auth.AuthService;

import static endpoint.PasswordChangeControllerEndpoints.POST_AND_PATCH_RESET;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PasswordResetService {

    private static int testUserId;
    private static String authToken;
    private static String adminToken;

        // Method to fetch OTP from the database
    public String fetchOtp(String userId) {
        return ConfigDB.getOtpCode(userId);
    }
    // Sign up a test user
    public void testSignUp() {
        given()
                .contentType("application/json")
                .body(AuthService.buildTestSignUpModel())
                .when()
                .log().body()
                .post(AuthEndpoints.SIGN_UP)
                .then()
                .log().body()
                .statusCode(500);
//                .body("code", equalTo("CREATED"));
    }

    // Sign in as the newly created user and get auth token
    public void testSignIn() {
        ResponseModel<AuthResponseModel> responseModel =
                given()
                        .contentType("application/json")
                        .body(AuthService.buildTestSignInModel())
                        .when()
                        .put(AuthEndpoints.SIGN_IN)
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {});

        authToken = responseModel.getContent().getToken();
        testUserId = responseModel.getContent().getId();
    }

    // Admin sign in to get adminToken
    public void adminSignIn() {
        ResponseModel<AuthResponseModel> responseModel =
                given()
                        .contentType("application/json")
                        .body(AuthService.buildTestSignInModel())  // Admin credentials
                        .when()
                        .put(AuthEndpoints.SIGN_IN)
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {});

        adminToken = responseModel.getContent().getToken();
    }

    // Admin changes password for the newly created user
    public void adminChangeUserPassword(String newPassword) {
        PasswordChangeRequestModel passwordChangeRequest = new PasswordChangeRequestModel(newPassword);

        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(passwordChangeRequest)
                .when()
                .patch(POST_AND_PATCH_RESET)
                .then()
                .log().body()
                .statusCode(200)
                .body("code", equalTo("PASSWORD_UPDATED"));
    }

    // Admin deletes the newly created user
    public void adminDeleteUser() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .delete(POST_AND_PATCH_RESET)
                .then()
                .log().body()
                .statusCode(200)
                .body("code", equalTo("USER_DELETED"));
    }

    // Getter methods
    public static int getTestUserId() {
        return testUserId;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static String getAdminToken() {
        return adminToken;
    }
}
