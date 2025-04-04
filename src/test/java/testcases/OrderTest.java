package testcases;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ResponseModel;
import model.order.OrderResponseModel;
import org.junit.jupiter.api.*;
import service.order.OrderService;

import java.util.List;

import static endpoint.OrderEndpoints.*;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    private static String authToken;
    private static int productId;
    private static int orderId;

    @BeforeAll
    public static void setup() {
        OrderService.setup();
        authToken = OrderService.getAuthToken();
        productId = OrderService.getProductId();

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
                    .extract().as(new TypeRef<ResponseModel<OrderResponseModel>>() {});

        OrderResponseModel order = response.getContent();
        orderId = order.getId();

    }

    @Test
    @Order(2)
    public void testGetOrderById() {
        System.out.println("Fetching Order ID: " + orderId);
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
                    .as(new TypeRef<ResponseModel<OrderResponseModel>>() {});

        Assertions.assertNotNull(response.getContent());
    }

    @Test
    @Order(3)
    public void testOrderGetAll() {
        System.out.println("Fetching all orders");
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
                    .as(new TypeRef<ResponseModel<List<OrderResponseModel>>>() {});

        Assertions.assertNotNull(response.getContent());
    }

    @Test
    @Order(4)
    public void testCancelOrderById() {
        System.out.println("Canceling Order ID: " + orderId);
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
