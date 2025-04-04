package service;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.feedback.FeedbackRequestModel;
import model.order.OrderResponseModel;
import service.auth.AuthService;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static io.restassured.RestAssured.given;

public class FeedbackService {

    private static String authToken;
    private static int orderId;


    public static void setup() {
        authToken = fetchAuthToken();
        orderId = fetchLastOrderId();
    }

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
                    .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {
                    });

        if (response == null || response.getContent() == null || response.getContent().getToken() == null) {
            throw new RuntimeException("AuthToken fetch failed! Response: " + response);
        }

        return response.getContent().getToken();
    }

    // Fetches the list of products and returns the ID of the first product
    private static int fetchLastOrderId() {
        ResponseModel<List<OrderResponseModel>> response =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                .when()
                    .get(PRODUCT_GET_ALL)
                .then()
                    .statusCode(200)
                    .log().body()
                    .extract().as(new TypeRef<ResponseModel<List<OrderResponseModel>>>() {
                    });

        // Assuming the list is not empty, return the ID of the first product
        return response.getContent().getLast().getId();
    }

    // Getter methods for accessing authToken and orderId
    public static String getAuthToken() {
        return authToken;
    }

    public static int getOrderId() {
        return orderId;
    }

    public static FeedbackRequestModel buildDefaultFeedbackRequest() {
        return FeedbackRequestModel.builder()
                .rating(4)
                .description("some description test")
                .build();
    }
}
