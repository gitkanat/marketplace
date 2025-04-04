package testcases;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import org.junit.jupiter.api.*;
import org.junit.platform.suite.api.AfterSuite;
import service.auth.AuthService;
import payload.auth.SignInRequestBuilder;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.AuthEndpoints.SIGN_UP;
import static endpoint.UserEndpoints.DELETE_BY_ID;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {

    private static int testUserId;
    private static String authToken;

    @Test
    @Order(1)
    public void testSignUpNegativeBalance_500Error() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(AuthService.buildTestSignUpModelNegativeBalance())
                .when()
                    .log().body()
                    .post(SIGN_UP)
                .then()
                    .log().body()
                    .statusCode(500);
    }

    @Test
    @Order(2)
    public void testSignUp() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(AuthService.buildTestSignUpModel())
                .when()
                    .log().body()
                    .post(SIGN_UP)
                .then()
                    .log().body()
                    .statusCode(200)
                    .body("code", equalTo("CREATED"));
    }

    @Test
    @Order(3)
    public void testSignUpNegativeDuplicate_500Error() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(AuthService.buildTestSignUpModel())
                .when()
                    .log().body()
                    .post(SIGN_UP)
                .then()
                    .log().body()
                    .statusCode(500);
    }

    @Test
    @Order(4)
    public void testSignIn_NoEmail() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder().withEmail(null).build()) // No email
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed
    }

    @Test
    @Order(5)
    public void testSignIn_NoPassword() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder().withPassword(null).build()) // No password
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed
    }

    @Test
    @Order(6)
    public void testSignIn_WrongEmail() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder().withEmail("wrong.email@example.com").build()) // Wrong email
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed (e.g., 401 Unauthorized)
    }

    @Test
    @Order(7)
    public void testSignIn_WrongPassword() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder().withPassword("wrongPassword").build()) // Wrong password
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed (e.g., 401 Unauthorized)
    }

    @Test
    @Order(8)
    public void testSignIn_NoEmailNoPassword() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder().withEmail(null).withPassword(null).build()) // No email and no password
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed
    }

    @Test
    @Order(9)
    public void testSignIn_WrongEmailWrongPassword() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(SignInRequestBuilder.builder()
                            .withEmail("wrong.email@example.com")
                            .withPassword("wrongPassword")
                            .build()) // Wrong email and wrong password
                .when()
                    .log().body()
                    .post(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(403); // Adjust status code as needed
    }

    @Test
    @Order(10)
    public void testSignIn() {
        ResponseModel<AuthResponseModel> responseModel = RestAssured
                .given()
                    .contentType("application/json")
                    .body(AuthService.buildTestSignInModel())
                .when()
                    .put(SIGN_IN)
                .then()
                    .log().body()
                    .statusCode(200)
                    .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {
                });
        authToken = responseModel.getContent().getToken();
        testUserId = responseModel.getContent().getId();
    }

    @AfterSuite
    public static void testDeleteUser() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .queryParam("id", testUserId)  // This will append `?id=value`
                .when()
                    .delete(DELETE_BY_ID);
    }

}



