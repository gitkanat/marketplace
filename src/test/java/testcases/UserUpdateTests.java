package testcases;

import config.Config;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.user.UserModel;
import org.junit.jupiter.api.*;
import payload.user.UserRequestBuilder;
import service.auth.AuthService;
import service.auth.AuthServiceVerTwo;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.AuthEndpoints.SIGN_UP;
import static endpoint.UserEndpoints.*;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserUpdateTests {
    private static int testUserId;
    private static String authToken;

    @BeforeAll
    public void setup() {

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


    @Test
    @Order(3)
    public void testUpdateUser_AsUser_ExistingEmail() {
        UserModel validUpdate = UserRequestBuilder.builder()
                .withFullName("Kanat Asanbekov")
                .withEmail(Config.getProperty("test.user.email"))
                .withPassword("Password123!")
                .withPhoneNumber("+996501111111")
                .withBalance(1.1)
                .withAddress("Updated Address")
                .build();

        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .queryParam("id", testUserId)
                    .body(validUpdate)
                .when()
                    .put(UPDATE)
                .then()
                    .log().body()
                    .statusCode(409);
    }

    @Test
    @Order(4)
    public void testUpdateUser_AsAdmin() {
        UserModel validUpdate = UserRequestBuilder.builder()
                .withFullName("Kanat Asanbekov")
                .withEmail(Config.getProperty("test.user.update.email"))
                .withPassword("Password123!")
                .withPhoneNumber("+996501111111")
                .withBalance(1.1)
                .withAddress("Updated Address")
                .build();

        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .queryParam("id", testUserId)
                    .body(validUpdate)
                .when()
                    .put(UPDATE)
                .then()
                    .log().body()
                    .statusCode(200);
    }

    @Test
    @Order(6)
    public void testGetUserById_AsUser_NoAccess() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .queryParam("id", testUserId)
                .when()
                    .get(GET_BY_ID)
                .then()
                    .log().body()
                    .statusCode(403);
    }

    @Test
    @Order(7)
    public void testGetUserById_AsUser_GetAllAccess_NegativeCase() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .queryParam("id", testUserId)
                .when()
                    .get(GET_ALL)
                .then()
                    .log().body()
                    .statusCode(403);
    }

    @Test
    @Order(8)
    public void testGetUserById_AsAdmin_GetAllAccess_PositiveCase() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                .when()
                    .get(GET_ALL)
                .then()
                    .log().body()
                    .statusCode(200);
    }

    @Test
    @Order(5)
    public void testGetUserById_AsAdmin_HasAccess() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .queryParam("id", testUserId)
                .when()
                    .get(GET_BY_ID)
                .then()
                    .log().body()
                    .statusCode(200);
    }

    @AfterAll
    public static void testDeleteUser() {
        RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .queryParam("id", testUserId)
                .when()
                    .delete(DELETE_BY_ID);
    }
}

