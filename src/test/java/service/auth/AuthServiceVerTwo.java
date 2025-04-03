package service.auth;
import config.Config;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import model.ResponseModel;
import model.auth.AuthResponseModel;

import java.util.HashMap;
import java.util.Map;

import static endpoint.AuthEndpoints.SIGN_IN;

public class AuthServiceVerTwo {
    public static String authTokenAdmin;
    public static String authTokenUser;
    public static String authTokenRich;


    public static String getAuthTokenForRole(String role) {
        String email = Config.getProperty("test." + role + ".email");
        String password = Config.getProperty("test." + role + ".password");

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        ResponseModel<AuthResponseModel> responseModel = RestAssured
                .given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .put(SIGN_IN)
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {});

        return responseModel.getContent().getToken();
    }


    public static void initializeTokens() {
        authTokenAdmin = getAuthTokenForRole("admin");
        authTokenUser = getAuthTokenForRole("user");
        authTokenRich = getAuthTokenForRole("rich");
        System.out.println("authTokenAdmin: " + authTokenAdmin);
        System.out.println("authTokenUser: " + authTokenUser);
        System.out.println("authTokenRich: " + authTokenRich);
    }
}


