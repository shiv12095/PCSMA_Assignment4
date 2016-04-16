package in.ac.iiitd.pcsma.chatclient.rest.client;


import in.ac.iiitd.pcsma.chatclient.rest.service.BackEndAPI;
import retrofit.RestAdapter;

import in.ac.iiitd.pcsma.chatclient.commons.Constants;

/**
 * Created by shiv on 16/4/16.
 */
public class BackEndAPIClient {


    private String BASE_URL = Constants.BACKEND_URL;

    private BackEndAPI backEndAPI;

    public BackEndAPIClient(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        backEndAPI = restAdapter.create(BackEndAPI.class);
    }

    public BackEndAPI getService(){
        return this.backEndAPI;
    }

}
