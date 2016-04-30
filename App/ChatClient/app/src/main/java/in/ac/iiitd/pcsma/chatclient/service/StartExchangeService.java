package in.ac.iiitd.pcsma.chatclient.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import in.ac.iiitd.pcsma.chatclient.adapter.DBAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rabbitmq.MessageConsumer;

/**
 * Created by alan on 1/5/16.
 */
public class StartExchangeService extends Service {

    private String MESSAGE_QUEUE;
    private MessageConsumer mConsumer;
    private SharedPreferences sharedPreferences;

    private String QUEUE_NAME = "myqueue";
    private String EXCHANGE_NAME = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DBAdapter dbAdapter = new DBAdapter();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        MESSAGE_QUEUE = sharedPreferences.getString(Constants.USER_ID, null);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            EXCHANGE_NAME = extras.getString(Constants.EXCHANGE_NAME);
        }
        try {
            // Create the consumer
            mConsumer = new MessageConsumer("192.168.48.246", EXCHANGE_NAME, "fanout");
            final String friendId = EXCHANGE_NAME.replace("EXCHANGE_", "").replace(MESSAGE_QUEUE, "");
            new consumerconnect().execute();
            // register for messages
            mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler() {
                public void onReceiveMessage(byte[] message) {
                    String text = "";
                    try {
                        text = new String(message, "UTF8");
                        System.out.println("Here :" + friendId + "\t" + EXCHANGE_NAME);
                        dbAdapter.insertIntoPrivateChat(friendId, text);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
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
