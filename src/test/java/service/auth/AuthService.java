package service.auth;

import config.Config;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.auth.SignInRequestModel;
import model.auth.SignUpRequestModel;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;

public class AuthService {


    private static String token;

    private static final String USER_EMAIL = Config.getProperty("test.user.email");
    private static final String USER_RICH_EMAIL = Config.getProperty("test.rich.email");
    private static final String USER_PASSWORD = Config.getProperty("test.user.password");

    private static final String TEST_FULL_NAME = Config.getProperty("test.full_name");
    private static final String TEST_PHONE_NUMBER = Config.getProperty("test.phone_number");
    private static final String TEST_BALANCE = Config.getProperty("test.balance");
    private static final String TEST_ADDRESS = Config.getProperty("test.address");
    private static final String TEST_BALANCE_NEGATIVE = Config.getProperty("test.balance.negative");


    public static String getToken() {
        if (token == null || !isTokenValid(token)) {
            token = generateToken();
        }
        return token;
    }

    public static SignInRequestModel buildTestSignInModel() {
        SignInRequestModel signInRequestModel = new SignInRequestModel();

        signInRequestModel.setEmail(USER_EMAIL);
        signInRequestModel.setPassword(USER_PASSWORD);

        return signInRequestModel;
    }
    public static SignInRequestModel buildTestSignInRichUserModel() {
        SignInRequestModel signInRequestModel = new SignInRequestModel();

        signInRequestModel.setEmail(USER_RICH_EMAIL);
        signInRequestModel.setPassword(USER_PASSWORD);

        return signInRequestModel;
    }

    public static SignUpRequestModel buildTestSignUpModel() {
        SignUpRequestModel signUpRequestModel = new SignUpRequestModel();

        signUpRequestModel.setFullName(TEST_FULL_NAME);
        signUpRequestModel.setEmail(USER_EMAIL);
        signUpRequestModel.setPassword(USER_PASSWORD);
        signUpRequestModel.setPhoneNumber(TEST_PHONE_NUMBER);
        signUpRequestModel.setBalance(Double.parseDouble(TEST_BALANCE));
        signUpRequestModel.setAddress(TEST_ADDRESS);

        return signUpRequestModel;
    }

    public static SignUpRequestModel buildTestSignUpModelNegativeBalance() {
        SignUpRequestModel signUpRequestModel = new SignUpRequestModel();

        signUpRequestModel.setFullName(TEST_FULL_NAME);
        signUpRequestModel.setEmail(USER_EMAIL);
        signUpRequestModel.setPassword(USER_PASSWORD);
        signUpRequestModel.setPhoneNumber(TEST_PHONE_NUMBER);
        signUpRequestModel.setBalance(Double.parseDouble(TEST_BALANCE_NEGATIVE));
        signUpRequestModel.setAddress(TEST_ADDRESS);

        return signUpRequestModel;
    }

    private static boolean isTokenValid(String token) {
        Response response = RestAssured
                .given()
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .get(PRODUCT_GET_ALL);

        return response.statusCode() == 200;

    }

    private static String generateToken() {
        SignInRequestModel signInRequestModel = new SignInRequestModel();

        signInRequestModel.setEmail(USER_EMAIL);
        signInRequestModel.setPassword(USER_PASSWORD);


        ResponseModel<AuthResponseModel> responseModel = RestAssured
                .given()
                    .contentType("application/json")
                    .body(signInRequestModel)
                    .post(SIGN_IN)
                    .as(new TypeRef<>() {
                });
        System.out.println("lkdjlkfd lkds " + responseModel.getCode());

        if (!responseModel.getCode().equals("OK")) {
            throw new RuntimeException("Authentication failed");
        }
        return responseModel.getContent().getToken();

    }


}


