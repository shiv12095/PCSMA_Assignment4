package in.ac.iiitd.pcsma.chatclient.rest.response;

/**
 * Created by shiv on 16/4/16.
 */
public class RegisterUserResponse {

    private String accessToken;
    private int validCredentials;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getValidCredentials() {
        return validCredentials;
    }

    public void setValidCredentials(int validCredentials) {
        this.validCredentials = validCredentials;
    }
}
