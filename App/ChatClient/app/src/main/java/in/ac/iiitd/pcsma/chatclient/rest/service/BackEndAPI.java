package in.ac.iiitd.pcsma.chatclient.rest.service;

import java.util.Map;

import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rest.response.RegisterUserResponse;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by shiv on 16/4/16.
 */
public interface BackEndAPI {

    @FormUrlEncoded
    @POST(Constants.REGISTER_ENDPOINT)
    public void register(@FieldMap Map<String, String> options, Callback<RegisterUserResponse> callback);

    @GET(Constants.LOGIN_ENDPOINT)
    public void login(@QueryMap Map<String, String> options, Callback<RegisterUserResponse> callback);

}
