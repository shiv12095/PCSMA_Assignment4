package in.ac.iiitd.pcsma.chatclient.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.adapter.ChatAdapter;
import in.ac.iiitd.pcsma.chatclient.adapter.DBAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.pojo.ChatMessage;
import in.ac.iiitd.pcsma.chatclient.rabbitmq.MessageConsumer;

public class TextActivity extends AppCompatActivity {

    private MessageConsumer mConsumer;
    private ListView messagesContainer;
    private EditText messageET;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatHistory;
    private String QUEUE_NAME = "myqueue";
    private String EXCHANGE_NAME = "test";
    private String message = "";
    private String name = "";
    private String friendId;
    private String userId;
    private String routingKey;
    private SharedPreferences sharedPreferences;
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        dbAdapter = new DBAdapter();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        friendId = "";
        if (extras != null) {
            friendId = extras.getString(Constants.FRIEND_ID);
        }
        userId = sharedPreferences.getString(Constants.USER_ID, null);
        name = userId;
        routingKey = "";
        QUEUE_NAME = "QUEUE_" + userId;
        EXCHANGE_NAME = "EXCHANGE_" + getQueueName(userId, friendId);
        dbAdapter.insertIntoExchanges(EXCHANGE_NAME, "PRIVATE");

        messagesContainer = (ListView) findViewById(R.id.activity_messages_container);
        messageET = (EditText) findViewById(R.id.activity_text_message);
        sendButton = (Button) findViewById(R.id.activity_text_send);

        loadHistory(friendId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                message = name + ": " + messageText;
                new send().execute(message);
                messageET.setText("");
            }
        });

         // Create the consumer
         mConsumer = new MessageConsumer("192.168.48.246", EXCHANGE_NAME, "fanout");
         new consumerconnect().execute();
         // register for messages
         mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {
             public void onReceiveMessage(byte[] message) {
                 String text = "";
                 try {
                     text = new String(message, "UTF8");
                 } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                 }
                 ChatMessage chatMessage = new ChatMessage();
                 String checkId = text.split(":")[0];
                 text = text.replace(checkId + ": ", "");
                 if (checkId.equals(friendId)) {
                     chatMessage.setMe(false);
                 } else {
                     chatMessage.setMe(true);
                 }
                 chatMessage.setMessage(text);
                 displayMessage(chatMessage);
             }
         });
    }

    public void displayMessage(ChatMessage message) {
        chatAdapter.add(message);
        chatAdapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadHistory(String friendId) {
        chatHistory = new ArrayList<ChatMessage>();
        List<String> previousMessages = dbAdapter.getAllChatsFromFriend(friendId);
        for (String prevMessage : previousMessages) {
            ChatMessage msg = new ChatMessage();
            String checkId = prevMessage.split(":")[0];
            prevMessage = prevMessage.replace(checkId + ": ", "");
            if (checkId.equals(friendId)) {
                msg.setMe(false);
            } else {
                msg.setMe(true);
            }
            System.out.println(prevMessage);
            msg.setMessage(prevMessage);
            chatHistory.add(msg);
        }

        chatAdapter = new ChatAdapter(TextActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(chatAdapter);

        for(int i=0; i< chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }



    private class send extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUsername("rabbituser");
                factory.setPassword("rabbit@123");
                factory.setHost("192.168.48.246");
                System.out.println("" + factory.getHost() + factory.getPort() + factory.getRequestedHeartbeat() + factory.getUsername());
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String tempstr = "";
                for (int i = 0; i < Message.length; i++)
                    tempstr += Message[i];

                channel.basicPublish(EXCHANGE_NAME, routingKey, null,
                        tempstr.getBytes());

                channel.close();

                connection.close();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }

    private class consumerconnect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {
                // Connect to broker
                mConsumer.connectToRabbitMQ(routingKey);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new consumerconnect().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConsumer.dispose();
    }

    private String getQueueName(String userId, String friendId) {
        String queueName;
        if (userId.compareTo(friendId) < 0) {
            queueName = userId + friendId;
        } else {
            queueName = friendId + userId;
        }
        return queueName;
    }
}