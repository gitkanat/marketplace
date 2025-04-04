package model.order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import model.product.ProductResponseModel;
import model.user.UserModel;


public class OrderResponseModel {
    private int id;
    private String createdAt;
    private String status;
    private ProductResponseModel productResponseDto;
    private UserModel userResponseDto;

    public OrderResponseModel() {
    }

    public OrderResponseModel(int id, String createdAt, String status, ProductResponseModel productResponseDto, UserModel userResponseDto) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.productResponseDto = productResponseDto;
        this.userResponseDto = userResponseDto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductResponseModel getProductResponseDto() {
        return productResponseDto;
    }

    public void setProductResponseDto(ProductResponseModel productResponseDto) {
        this.productResponseDto = productResponseDto;
    }

    public UserModel getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(UserModel userResponseDto) {
        this.userResponseDto = userResponseDto;
    }

    @Override
    public String toString() {
        return "OrderResponseModel{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", status='" + status + '\'' +
                ", productResponseDto=" + productResponseDto +
                ", userResponseDto=" + userResponseDto +
                '}';
    }
}
