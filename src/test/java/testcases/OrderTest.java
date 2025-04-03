package testcases;


import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.order.OrderResponseModel;
import model.product.ProductResponseModel;
import model.user.UserModel;
import org.junit.jupiter.api.*;
import service.auth.AuthService;
import service.auth.AuthServiceVerTwo;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.OrderEndpoints.ORDER_CREATE;
import static endpoint.OrderEndpoints.ORDER_GET_BY_ID;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static endpoint.ProductEndpoints.PRODUCT_GET_BY_ID;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {


    private static int testUserId;
    private static String authToken;
    private static int productId;
    private static int orderId;

    @BeforeAll
    public static void setup() {
        // Assuming we need to get a productId before we can create an order
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
        testUserId = responseModel.getContent().getId();

        ResponseModel<List<ProductResponseModel>> response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .when()
                        .get(PRODUCT_GET_ALL)
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract()
                        .as(new TypeRef<ResponseModel<List<ProductResponseModel>>>() {
                        });
        List<ProductResponseModel> products = response.getContent();
        productId = products.getFirst().getId();
//        ResponseModel<UserModel> userResponse =
//                given()
//                        .contentType(ContentType.JSON)
//                        .header("Authorization", "Bearer " + authToken)
//                        .queryParam("productId", productId)  // Add any necessary parameters
//                        .when()
//                        .get(PRODUCT_GET_ALL)  // Use appropriate endpoint for balance retrieval
//                        .then()
//                        .statusCode(200)
//                        .log().body()
//                        .extract()
//                        .as(new TypeRef<ResponseModel<UserModel>>() {});
//
//// Extract the balance from the response
//        balance = userResponse.getContent().getBalance();
//        System.out.println("User Balance: " + balance);


    }


    @Test
    @Order(1)
    public void createOrderTest() {
        System.out.println("Product ID: " + productId);

        System.out.println("User Id = " + testUserId);
        System.out.println("URL = " + ORDER_CREATE);
        // Using the productId obtained from setup
        Response createOrderResponse =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .queryParam("productId", productId) // Using the productId from the setup
                        .when()
                        .post(ORDER_CREATE)
                        .then()
                        .statusCode(200)
                        .extract().response();
//        orderId = createOrderResponse.jsonPath().getInt("id");
        orderId = createOrderResponse.as(new TypeRef<ResponseModel<OrderResponseModel>>() {
        }).getContent().getId();

        // You can assert that the order was created correctly based on the response
    }

    @Test
    @Order(2)
    public void testGetOrderById() {
        System.out.println("Order ID: " + orderId);
        // Assuming you have the orderId from the previous test
        ResponseModel<OrderResponseModel> response =
                given()
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .queryParam("orderId", orderId)
                        .when()
                        .get(ORDER_GET_BY_ID)
                        .then()
                        .statusCode(200)
                        .log().body()
                        .extract()
                        .as(new TypeRef<ResponseModel<OrderResponseModel>>() {
                        });
        Assertions.assertNotNull(response.getContent());

    }
}
