package endpoint;

import config.Config;

public class FeedbackEndpoints {
    public static final String BASE_URL = Config.getProperty("test.base-url") + "/api/feedback";
    public static final String FEEDBACK_PUT_UPDATE = BASE_URL + "/update";
    public static final String FEEDBACK_POST_CREATE = BASE_URL + "/create";
    public static final String FEEDBACK_GET_BY_ID = BASE_URL + "/get-by-id";
    public static final String FEEDBACK_GET_ALL = BASE_URL + "/get-all";
    public static final String FEEDBACK_DELETE_BY_ID = BASE_URL + "/delete-by-id";
}


/*
PUT
/api/feedback/update

POST
/api/feedback/create

GET
/api/feedback/get-by-id

GET
/api/feedback/get-all

DELETE
/api/feedback/delete-by-id
 */