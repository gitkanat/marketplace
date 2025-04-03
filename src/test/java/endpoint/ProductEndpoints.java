package endpoint;

import config.Config;

public class ProductEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url") + "/api/product";
    public static final String PRODUCT_GET_ALL = BASE_URL + "/get-all";
    public static final String PRODUCT_UPDATE = BASE_URL + "/update";
    public static final String PRODUCT_CREATE = BASE_URL + "/create";
    public static final String PRODUCT_GET_IMAGE_BY_FILENAME = BASE_URL + "/get-image/{filename}";
    public static final String PRODUCT_GET_BY_ID = BASE_URL + "/get-by-id";
    public static final String PRODUCT_DELETE = BASE_URL + "/delete";

}
