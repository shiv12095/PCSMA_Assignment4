package in.ac.iiitd.pcsma.chatclient.commons;

/**
 * Created by shiv on 16/4/16.
 */
public class Constants {

    public static final String BACKEND_URL = "http://192.168.48.246:8080";

    public static final String REGISTER_ENDPOINT = "/register";

    public static final String GET_FRIENDS_ENDPOINT = "/getFriends";

    public static final String LOGIN_ENDPOINT = "/login";

    public static final String CREATE_USER_PROFILE_ENDPOINT = "/createProfile";

    public static final int PASSWORD_LENGTH = 6;

    public static final String JSON_PARAMETER_USERNAME = "username";

    public static final String JSON_PARAMETER_PASSWORD = "password";

    public static final String JSON_PARAMETER__PROFILE_NAME = "name";

    public static final String JSON_PARAMETER_PHONE_NUMBER = "number";

    public static final String JSON_PARAMETER_PHONE_NUMBERS = "phoneNumbers";

    public static final String SHARED_PREFS = "shared prefs";

    public static final String ACCESS_TOKEN = "accessToken";

    public static final int CODE_VALID_CREDENTIALS = 1;

    public static final int CODE_INVALID_CREDENTIALS = 0;
}
