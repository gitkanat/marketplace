package endpoint;

import config.Config;

public class OrderEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url") + "/api/order";
    public static final String ORDER_GET_ALL = BASE_URL + "/get-all";
    public static final String ORDER_CREATE = BASE_URL + "/create";
    public static final String ORDER_GET_BY_ID = BASE_URL + "/get-by-id";
    public static final String ORDER_CANCEL = BASE_URL + "/cancel-order";
}
