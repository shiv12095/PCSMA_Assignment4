package in.ac.iiitd.pcsma.chatclient.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.adapter.FriendListAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.item.UserProfile;
import in.ac.iiitd.pcsma.chatclient.rest.client.BackEndAPIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment {

    private BackEndAPIClient backEndAPIClient;
    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private FriendListAdapter friendListAdapter;
    private ArrayList<UserProfile> userProfileList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        backEndAPIClient = new BackEndAPIClient();
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfileList = new ArrayList<UserProfile>();

        friendListAdapter = new FriendListAdapter(getContext(), userProfileList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_friend_list_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(friendListAdapter);
        fetchFriends();
    }

    private void fetchFriends() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.ACCESS_TOKEN, sharedPreferences.getString(Constants.ACCESS_TOKEN, null));

        backEndAPIClient.getService().getFriends(parameters, new Callback<ArrayList<UserProfile>>() {
            @Override
            public void success(ArrayList<UserProfile> list, Response response) {
                HashSet<String> numberSet = getFriendPhoneNumberSet();
                for (UserProfile userProfile : list) {
                    if (numberSet.contains(userProfile.getNumber())) {
                        friendListAdapter.add(userProfile);
                        Log.d(getClass().toString(), userProfile.getName());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private HashSet<String> getFriendPhoneNumberSet() {
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        HashSet<String> numberSet = new HashSet<>();
        while (phones.moveToNext()) {
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numberSet.add(formatNumber(phoneNumber));
        }
        phones.close();
        return numberSet;
    }

    private String formatNumber (String number) {
        number = number.replaceAll("\\s+","");
        if (number.startsWith("+91")) {
            number = number.substring(3, number.length());
        }
        return number;
    }
}
