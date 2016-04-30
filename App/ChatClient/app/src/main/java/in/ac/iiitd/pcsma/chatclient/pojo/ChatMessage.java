package in.ac.iiitd.pcsma.chatclient.pojo;

/**
 * Created by alan on 30/4/16.
 */
public class ChatMessage {

    private long id;
    private boolean isMe;
    private String message;
    private Long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
