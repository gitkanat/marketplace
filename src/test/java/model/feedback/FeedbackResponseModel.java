package model.feedback;

import model.order.OrderResponseModel;
import model.user.UserModel;

public class FeedbackResponseModel {

        private int id;
        private int rating;
        private String description;
        private UserModel user;
        private OrderResponseModel order;

    public FeedbackResponseModel() {
    }

    public FeedbackResponseModel(int id, int rating, String description, UserModel user, OrderResponseModel order) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.user = user;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public OrderResponseModel getOrder() {
        return order;
    }

    public void setOrder(OrderResponseModel order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "FeedbackResponseModel{" +
                "id=" + id +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", user=" + user +
                ", order=" + order +
                '}';
    }
}
