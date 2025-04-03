package payload.auth;

import config.Config;
import model.auth.SignInRequestModel;

public class SignInRequestBuilder {

    private String email;
    private String password;

    private static final String USER_EMAIL = Config.getProperty("test.user.email");
    private static final String USER_PASSWORD = Config.getProperty("test.user.password");

    private SignInRequestBuilder() {
        this.email = USER_EMAIL;
        this.password = USER_PASSWORD;
    }

    public static SignInRequestBuilder builder() {
        return new SignInRequestBuilder();
    }

    public SignInRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public SignInRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public SignInRequestModel build() {
        SignInRequestModel signInRequestModel = new SignInRequestModel();
        signInRequestModel.setEmail(email);
        signInRequestModel.setPassword(password);
        return signInRequestModel;
    }

    public static SignInRequestModel buildTestSignInModel() {
        return builder().build();
    }
}
