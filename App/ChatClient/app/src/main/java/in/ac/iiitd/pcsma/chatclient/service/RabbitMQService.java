package in.ac.iiitd.pcsma.chatclient.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.List;

import in.ac.iiitd.pcsma.chatclient.activity.TextActivity;
import in.ac.iiitd.pcsma.chatclient.adapter.DBAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.commons.NotificationUtils;
import in.ac.iiitd.pcsma.chatclient.rabbitmq.MessageConsumer;

/**
 * Created by alan on 18/4/16.
 */
public class RabbitMQService extends Service {

    private String MESSAGE_QUEUE;
    private MessageConsumer mConsumer;
    private SharedPreferences sharedPreferences;

    private String QUEUE_NAME = "myqueue";
    private String EXCHANGE_NAME = "test";

    private NotificationUtils notificationUtils;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DBAdapter dbAdapter = new DBAdapter();
        notificationUtils = new NotificationUtils(getApplicationContext());
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        MESSAGE_QUEUE = sharedPreferences.getString(Constants.USER_ID, null);
        List<String> exchanges = dbAdapter.getAllExchanges();
        System.out.println(exchanges.toString());
        try {
            for (final String exchange : exchanges) {
                // Create the consumer
                mConsumer = new MessageConsumer("192.168.48.246", exchange, "fanout");
                final String friendId = exchange.replace("EXCHANGE_", "").replace(MESSAGE_QUEUE, "");
                new consumerconnect().execute();
                // register for messages
                mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {
                    public void onReceiveMessage(byte[] message) {
                        String text = "";
                        try {
                            text = new String(message, "UTF8");
                            String friendId = text.split(":")[0];
                            System.out.println("Here :" + friendId + "\t" + exchange);
                            dbAdapter.insertIntoPrivateChat(friendId, text);
                            Intent resultIntent = new Intent(getApplicationContext(), TextActivity.class);
                            resultIntent.putExtra(Constants.FRIEND_ID, friendId);
                            showNotificationMessage(getApplicationContext(), friendId, text, resultIntent);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private void showNotificationMessage(Context context, String userName, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(userName,message, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MyService", "onCreate callback called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("MyService", "onDestroy callback called");
    }

    private class consumerconnect extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... Message) {
            try {
                // Connect to broker
                mConsumer.connectToRabbitMQ("");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }
    }
}
