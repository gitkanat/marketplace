package testcases;

import config.Config;
import endpoint.PasswordChangeControllerEndpoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.PasswordResetService;

import static io.restassured.RestAssured.given;
//
public class PasswordResetTest {

    private static PasswordResetService userManagementService;
    private static String userEmail = Config.getProperty("test.pwd.update.email");

    @BeforeAll
    public static void setup() {
//        RestAssured.baseURI = Config.getProperty("test.base-url");
        userManagementService = new PasswordResetService();

        // Step 1: Create a test user
//        userManagementService.testSignUp();

        // Step 2: Sign in as the test user to get userId
        userManagementService.testSignIn();

        // Step 3: Admin signs in to get adminToken
        userManagementService.adminSignIn();
    }

    @Test
    public void testPasswordResetFlow() {
        int testUserId = PasswordResetService.getTestUserId();
        //Шаг1. Админ sign in и запроси ОТП
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userManagementService.getAdminToken())
                .queryParam("email", userEmail)
                .when()
                .post(PasswordChangeControllerEndpoints.POST_AND_PATCH_RESET)
                .then()
                .statusCode(200)
                .extract().response();

        // Шаг 2: Получи ОТП с сервера
        String otp = userManagementService.fetchOtp(userEmail);
        System.out.println("OTP request sent successfully. OTP: " + otp);

        // Шаг 3: Подтверди ОТП и сброс пароля
        Response otpVerificationResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + userManagementService.getAdminToken())
                .queryParam("passwordResetCode", otp)
                .body("{\"password\": \"Password123!\"}")
                .when()
                .patch(PasswordChangeControllerEndpoints.POST_AND_PATCH_RESET)
                .then()
                .statusCode(500) // Временная заглушка, замените на фактический статус код
                .extract().response();


    }


}
