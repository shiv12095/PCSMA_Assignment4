package in.ac.iiitd.pcsma.chatclient.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.adapter.FriendListAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.item.Friend;
import in.ac.iiitd.pcsma.chatclient.item.UserProfile;
import in.ac.iiitd.pcsma.chatclient.rest.client.BackEndAPIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivateChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivateChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateChatFragment extends Fragment {

    public PrivateChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_private_chat, container, false);
    }


}
