package in.ac.iiitd.pcsma.chatclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rest.client.BackEndAPIClient;
import in.ac.iiitd.pcsma.chatclient.rest.response.RegisterUserResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    private BackEndAPIClient backEndAPIClient;

    private EditText userNameEditText;
    private EditText passwordEditText;

    private Button loginButton;
    private Button registerButton;

    private String userName;
    private String password;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backEndAPIClient = new BackEndAPIClient();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);

        userNameEditText = (EditText) findViewById(R.id.activity_login_username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.activity_login_password_edit_text);

        loginButton = (Button) findViewById(R.id.activity_login_button);
        registerButton = (Button) findViewById(R.id.activity_register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    login();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    register();
                }
            }
        });
    }

    private boolean isValidInput(){
        userName = userNameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if (userName == null || userName.length() < 1) {
            Toast.makeText(LoginActivity.this, "Please enter a valid user name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.length() < Constants.PASSWORD_LENGTH) {
            Toast.makeText(LoginActivity.this, "Password must be at least " + Integer.toString(Constants.PASSWORD_LENGTH) + " characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void register() {
        backEndAPIClient.getService().register(getParameterHashMap(), new Callback<RegisterUserResponse>() {
            @Override
            public void success(RegisterUserResponse registerUserResponse, Response response) {
                Log.d(getClass().toString(), "Successfully registered user");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (registerUserResponse.getValidCredentials() == Constants.CODE_VALID_CREDENTIALS) {
                    editor.putString(Constants.ACCESS_TOKEN, registerUserResponse.getAccessToken());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Username unavailable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to register user");
            }
        });
    }

    private void login() {
        backEndAPIClient.getService().login(getParameterHashMap(), new Callback<RegisterUserResponse>() {
            @Override
            public void success(RegisterUserResponse registerUserResponse, Response response) {
                Log.d(getClass().toString(), "Successfully registered user");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (registerUserResponse.getValidCredentials() == Constants.CODE_VALID_CREDENTIALS) {
                    editor.putString(Constants.ACCESS_TOKEN, registerUserResponse.getAccessToken());
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to register user");
            }
        });
    }

    private HashMap<String, String> getParameterHashMap(){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER_PASSWORD, password);
        parameters.put(Constants.JSON_PARAMETER_USERNAME, userName);
        return parameters;
    }
}
