package in.ac.iiitd.pcsma.chatclient.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rest.client.BackEndAPIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText numberEditText;
    private Button createProfileButton;

    private SharedPreferences sharedPreferences;
    private BackEndAPIClient backEndAPIClient;

    private TelephonyManager telephonyManager;

    private String number;
    private String name;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        backEndAPIClient = new BackEndAPIClient();
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN, null);

        nameEditText = (EditText) findViewById(R.id.activity_profile_name_edit_text);
        numberEditText = (EditText) findViewById(R.id.activity_profile_phone_number_edit_text);
        createProfileButton = (Button) findViewById(R.id.activity_profile_create_profile_button);

        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    createProfile();
                }
            }
        });
    }

    private boolean isValidInput(){
        if(nameEditText.getText().toString() == null || nameEditText.getText().toString().isEmpty()){
            return false;
        }
        if(numberEditText.getText().toString() == null || numberEditText.getText().toString().isEmpty()){
            return false;
        }
        number = numberEditText.getText().toString();
        name = nameEditText.getText().toString();
        return true;
    }

    private void createProfile(){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER__PROFILE_NAME, name);
        parameters.put(Constants.JSON_PARAMETER_PHONE_NUMBER, number);
        parameters.put(Constants.ACCESS_TOKEN, accessToken);
        Log.d(getClass().toString(), accessToken);
        backEndAPIClient.getService().createProfile(parameters, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(Constants.ACCESS_TOKEN);
            }
        });
    }
}
