package service;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.feedback.FeedbackRequestModel;
import model.order.OrderResponseModel;
import model.product.ProductResponseModel;
import org.junit.jupiter.api.BeforeAll;
import service.auth.AuthService;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.OrderEndpoints.ORDER_CREATE;
import static endpoint.OrderEndpoints.ORDER_GET_BY_ID;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static io.restassured.RestAssured.given;

public class FeedbackService {

    private static String authToken;
    private static int orderId;

    public static void setup() {
        // Step 1: Auth
        ResponseModel<AuthResponseModel> responseModel = RestAssured
                .given()
                .contentType("application/json")
                .body(AuthService.buildTestSignInRichUserModel())
                .when()
                .put(SIGN_IN)
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {
                });

        authToken = responseModel.getContent().getToken();

        // Step 2: Get a product to order
        ResponseModel<List<ProductResponseModel>> productsResponse =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .when()
                        .get(PRODUCT_GET_ALL)
                        .then()
                        .statusCode(200)
                        .extract().as(new TypeRef<ResponseModel<List<ProductResponseModel>>>() {
                        });

        int productId = productsResponse.getContent().getFirst().getId();

        // Step 3: Create order
        ResponseModel<OrderResponseModel> orderResponse =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .queryParam("productId", productId)
                        .when()
                        .post(ORDER_CREATE)
                        .then()
                        .statusCode(200)
                        .extract().as(new TypeRef<ResponseModel<OrderResponseModel>>() {
                        });

        orderId = orderResponse.getContent().getId();
    }
    public static FeedbackRequestModel buildDefaultFeedbackRequest() {
        return FeedbackRequestModel.builder()
                .rating(4)
                .description("some description test")
                .build();
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static int getOrderId() {
        return orderId;
    }
}
