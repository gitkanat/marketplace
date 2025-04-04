package service.order;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.product.ProductResponseModel;
import service.auth.AuthService;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static io.restassured.RestAssured.given;

public class OrderService {

    private static String authToken;
    private static int productId;

    // Setup method to authenticate and fetch the first product ID
    public static void setup() {
        authToken = fetchAuthToken();
        productId = fetchFirstProductId();
    }

    // Fetches authentication token by signing in a user
    private static String fetchAuthToken() {
        ResponseModel<AuthResponseModel> response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(AuthService.buildTestSignInRichUserModel())
                .when()
                .put(SIGN_IN)
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {});

        return response.getContent().getToken();
    }

    // Fetches the list of products and returns the ID of the first product
    private static int fetchFirstProductId() {
        ResponseModel<List<ProductResponseModel>> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(PRODUCT_GET_ALL)
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(new TypeRef<ResponseModel<List<ProductResponseModel>>>() {});

        // Assuming the list is not empty, return the ID of the first product
        return response.getContent().get(0).getId();
    }

    // Getter methods for accessing authToken and productId
    public static String getAuthToken() {
        return authToken;
    }

    public static int getProductId() {
        return productId;
    }
}


