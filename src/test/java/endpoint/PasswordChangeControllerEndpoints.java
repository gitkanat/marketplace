package endpoint;

import config.Config;

public class PasswordChangeControllerEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url") + "/api";
    public static final String POST_AND_PATCH_RESET = BASE_URL + "/reset";
}
