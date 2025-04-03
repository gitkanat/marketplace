package endpoint;

import config.Config;

public class AuthEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url");
    public static final String SIGN_IN = BASE_URL + "/api/auth/sign-in";
    public static final String SIGN_UP = BASE_URL + "/api/auth/sign-up";
}
