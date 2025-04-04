package model.feedback;

public class FeedbackRequestModel {
    private int rating;
    private String description;

    public FeedbackRequestModel() {}

    public FeedbackRequestModel(int rating, String description) {
        this.rating = rating;
        this.description = description;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int rating;
        private String description;

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public FeedbackRequestModel build() {
            return new FeedbackRequestModel(rating, description);
        }
    }
}
