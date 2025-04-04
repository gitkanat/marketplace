//package service;
//
//import io.restassured.RestAssured;
//import io.restassured.common.mapper.TypeRef;
//import io.restassured.http.ContentType;
//import model.ResponseModel;
//import model.auth.AuthResponseModel;
//import model.feedback.FeedbackRequestModel;
//import model.order.OrderResponseModel;
//import model.product.ProductResponseModel;
//import service.auth.AuthService;
//
//import java.util.List;
//
//import static endpoint.AuthEndpoints.SIGN_IN;
//import static endpoint.OrderEndpoints.ORDER_CREATE;
//import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
//import static io.restassured.RestAssured.given;
//
//public class FeedbackService {
//
//    private static String authToken;
//    private static int orderId;
//
//    public static void setup() {
//        // Step 1: Auth
//        ResponseModel<AuthResponseModel> responseModel = RestAssured
//                .given()
//                .contentType("application/json")
//                .body(AuthService.buildTestSignInRichUserModel())
//                .when()
//                .put(SIGN_IN)
//                .then()
//                .log().body()
//                .statusCode(200)
//                .extract().as(new TypeRef<ResponseModel<AuthResponseModel>>() {
//                });
//
//        authToken = responseModel.getContent().getToken();
//
//        // Step 2: Get a product to order
//        ResponseModel<List<ProductResponseModel>> productsResponse =
//                given()
//                        .contentType(ContentType.JSON)
//                        .header("Authorization", "Bearer " + authToken)
//                        .when()
//                        .get(PRODUCT_GET_ALL)
//                        .then()
//                        .statusCode(200)
//                        .extract().as(new TypeRef<ResponseModel<List<ProductResponseModel>>>() {
//                        });
//
//        int productId = productsResponse.getContent().getFirst().getId();
//
//        // Step 3: Create order
//        ResponseModel<OrderResponseModel> orderResponse =
//                given()
//                        .contentType(ContentType.JSON)
//                        .header("Authorization", "Bearer " + authToken)
//                        .queryParam("productId", productId)
//                        .when()
//                        .post(ORDER_CREATE)
//                        .then()
//                        .statusCode(200)
//                        .extract().as(new TypeRef<ResponseModel<OrderResponseModel>>() {
//                        });
//
//        orderId = orderResponse.getContent().getId();
//    }
//
//    public static FeedbackRequestModel buildDefaultFeedbackRequest() {
//        return FeedbackRequestModel.builder()
//                .rating(4)
//                .description("some description test")
//                .build();
//    }
//
//    public static String getAuthToken() {
//        return authToken;
//    }
//
//    public static int getOrderId() {
//        return orderId;
//    }
//}
//
//
package service;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.feedback.FeedbackRequestModel;
import model.order.OrderResponseModel;
import model.product.ProductResponseModel;
import service.auth.AuthService;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.OrderEndpoints.ORDER_CREATE;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static io.restassured.RestAssured.given;

public class FeedbackService {

    private static String authToken;
    private static int orderId;

    // Setup method to authenticate and fetch the first product ID
    public static void setup() {
        authToken = fetchAuthToken();
        orderId = fetchFirstProductId();
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
        ResponseModel<List<OrderResponseModel>> response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(PRODUCT_GET_ALL)
                .then()
                .statusCode(200)
                .log().body()
                .extract().as(new TypeRef<ResponseModel<List<OrderResponseModel>>>() {});

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
