package payload.user;


import model.user.UserModel;

public class UserRequestBuilder {

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private double balance;
    private String address;

    private UserRequestBuilder() {
    }

    public static UserRequestBuilder builder() {
        return new UserRequestBuilder();
    }

    public UserRequestBuilder withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRequestBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserRequestBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public UserRequestBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public UserModel build() {
        UserModel userModel = new UserModel();
        userModel.setFullName(fullName);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setBalance(balance);
        userModel.setAddress(address);
        return userModel;
    }
}