package in.ac.iiitd.pcsma.chatclient.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.UnsupportedEncodingException;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rabbitmq.MessageConsumer;

public class TextActivity extends AppCompatActivity {

    private MessageConsumer mConsumer;
    private TextView mOutput;
    private String QUEUE_NAME = "myqueue";
    private String EXCHANGE_NAME = "test";
    private String message = "";
    private String name = "";
    private String friendId;
    private String userId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        friendId = "";
        if (extras != null) {
            friendId = extras.getString(Constants.FRIEND_ID);
        }
        userId = sharedPreferences.getString(Constants.USER_ID, null);
        name = userId;
        QUEUE_NAME = getQueueName(userId, friendId);
        EXCHANGE_NAME = "EXCHANGE_" + QUEUE_NAME;

        final EditText msg = (EditText) findViewById(R.id.activity_text_message);
        Button button = (Button) findViewById(R.id.activity_text_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                message = name + ": " + msg.getText().toString();
                new send().execute(message);
                msg.setText("");
            }
        });

        // The output TextView we'll use to display messages
        mOutput = (TextView) findViewById(R.id.activity_text_output);

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
                mOutput.append("\n" + text);
            }
        });
    }

    private class send extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... Message) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setUsername("rabbituser");
                factory.setPassword("rabbit@123");
                factory.setHost("192.168.48.246");
                System.out.println(""+factory.getHost()+factory.getPort()+factory.getRequestedHeartbeat()+factory.getUsername());
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                String tempstr = "";
                for (int i = 0; i < Message.length; i++)
                    tempstr += Message[i];

                channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null,
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
                mConsumer.connectToRabbitMQ();
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
