package testcases;


import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ResponseModel;
import model.auth.AuthResponseModel;
import model.order.OrderResponseModel;
import model.product.ProductResponseModel;
import org.junit.jupiter.api.*;
import service.auth.AuthService;

import java.util.List;

import static endpoint.AuthEndpoints.SIGN_IN;
import static endpoint.OrderEndpoints.*;
import static endpoint.ProductEndpoints.PRODUCT_GET_ALL;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {


    private static String authToken;
    private static int productId;
    private static int orderId;

    @BeforeAll
    public static void setup() {
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


    }


    @Test
    @Order(1)
    public void createOrderTest() throws InterruptedException {

        ResponseModel<OrderResponseModel> response =
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

        OrderResponseModel order = response.getContent();
        double balance = order.getUserResponseDto().getBalance();
        orderId = order.getId();


    }

    @Test
    @Order(2)
    public void testGetOrderById() {
        System.out.println("Order ID: " + orderId);
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
    @Test
    @Order(3)
    public void testOrderGetAll() {
        System.out.println("Order ID: " + orderId);
        ResponseModel<List<OrderResponseModel>> response =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                .when()
                    .get(ORDER_GET_ALL)
                .then()
                    .statusCode(200)
                    .log().body()
                    .extract()
                    .as(new TypeRef<ResponseModel<List<OrderResponseModel>>>() {
                    });

        Assertions.assertNotNull(response.getContent());

    }


    @Test
    @Order(4)
    public void testCancelOrderById() {
        System.out.println("Order ID: " + orderId);
        Response response =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .queryParam("orderId", orderId)
                .when()
                    .put(ORDER_CANCEL)
                .then()
                    .statusCode(200)
                    .log().body()
                    .extract()
                    .response();
        Assertions.assertEquals(response.getBody().asString(), "{\"status\":\"OK\",\"message\":\"Order canceled\"}");

    }
}
