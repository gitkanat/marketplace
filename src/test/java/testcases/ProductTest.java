package testcases;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.ResponseModel;
import model.product.ProductResponseModel;
import org.junit.jupiter.api.*;
import service.auth.AuthServiceVerTwo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static endpoint.ProductEndpoints.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    private int getId;

    @Test
    @Order(1)
    public void testCreateProduct_AsAdmin() {
        String productRequestJson = "{\"name\": \"Alamedin2\", \"price\": 100, \"description\": \"test\", \"count\": 5}";

        File fileToUpload = new File("src/test/resources/image/iphone.jpeg");
        if (!fileToUpload.exists()) {
            throw new RuntimeException("File not found: " + fileToUpload.getAbsolutePath());
        }
        ResponseModel<ProductResponseModel> responseModel =
                given()
                    .contentType("multipart/form-data")
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .queryParam("productRequest", productRequestJson)
                    .multiPart("file", fileToUpload, "image/jpeg")
                .when()
                    .post(PRODUCT_CREATE)
                .then()
                    .log().body()
                    .statusCode(201).extract().as(new TypeRef<ResponseModel<ProductResponseModel>>() {
                });
        getId = responseModel.getContent().getId();
    }

    @Test
    @Order(2)
    public void testUpdateProduct_AsAdmin() {
        given()
            .contentType("multipart/form-data")
            .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
            .queryParam("id", getId)
            .queryParam("productRequest", "{\"name\": \"Alamedin5\", \"price\": 100, \"description\": \"test\", \"count\": 5}")
            .multiPart("file", new File("src/test/resources/image/iphone.jpeg"), "iphone/jpeg")
        .when()
            .put(PRODUCT_UPDATE)
        .then()
            .statusCode(200);

    }


    @Test
    @Order(3)
    public void testProduct_GetById() {
        ResponseModel<ProductResponseModel> response =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .queryParam("id", getId)
                .when()
                    .get(PRODUCT_GET_BY_ID)
                .then()
                    .statusCode(200)
                    .log().body()
                    .extract().as(new TypeRef<ResponseModel<ProductResponseModel>>() {
                });
        assertEquals(getId, response.getContent().getId());

    }
    @Test
    @Order(4)
    public void testProduct_GetByImageFileName() {
        String fileName = "5e81167c-df0d-48c9-915a-db030d8a9ff9_iphone14pro.jpg";
        byte[] imageResponse =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                    .pathParam("filename", fileName)
                .when()
                    .get(PRODUCT_GET_IMAGE_BY_FILENAME)
                .then()
                    .statusCode(200)
                    .extract().asByteArray();

        Assertions.assertTrue(imageResponse.length > 0, "Image should not be empty.");


        try (FileOutputStream fos = new FileOutputStream("received_image.jpg")) {
            fos.write(imageResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void testProduct_GetALL() {
        ResponseModel<List<ProductResponseModel>> response =
                given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
                .when()
                    .get(PRODUCT_GET_ALL)
                .then()
                    .statusCode(200)
                    .log().body()
                    .extract()
                    .as(new TypeRef<ResponseModel<List<ProductResponseModel>>>() {
                });
        List<ProductResponseModel> products = response.getContent();

        Assertions.assertFalse(products.isEmpty(), "Список продуктов пуст!");

        Assertions.assertNotNull(products.get(0).getId(), "ID первого продукта null");
        Assertions.assertNotNull(products.get(0).getName(), "Название первого продукта null");
    }

    @AfterAll
    public void testDeleteProductById() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + AuthServiceVerTwo.getAuthTokenForRole("admin"))
            .queryParam("id", getId)
        .when()
            .delete(PRODUCT_DELETE)
        .then()
            .statusCode(204)
            .log().body();

    }

}
