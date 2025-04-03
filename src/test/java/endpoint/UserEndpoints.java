package endpoint;

import config.Config;

public class UserEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url") + "/api/user";
    public static final String GET_ALL = BASE_URL + "/get-all";
    public static final String GET_BY_ID = BASE_URL + "/by-id";
    public static final String DELETE_BY_ID = BASE_URL + "/delete-by-id";
    public static final String UPDATE = BASE_URL + "/update";


}
