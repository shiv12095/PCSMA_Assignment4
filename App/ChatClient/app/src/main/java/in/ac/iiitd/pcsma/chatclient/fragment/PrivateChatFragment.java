package in.ac.iiitd.pcsma.chatclient.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.adapter.DBAdapter;
import in.ac.iiitd.pcsma.chatclient.adapter.PrivateChatAdapter;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
import in.ac.iiitd.pcsma.chatclient.rest.client.BackEndAPIClient;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrivateChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrivateChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateChatFragment extends Fragment {

    private BackEndAPIClient backEndAPIClient;
    private SharedPreferences sharedPreferences;
    private DBAdapter dbAdapter;

    private RecyclerView recyclerView;
    private PrivateChatAdapter privateChatAdapter;
    private ArrayList<String> privateChatList;

    public PrivateChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbAdapter = new DBAdapter();
        backEndAPIClient = new BackEndAPIClient();
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_private_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        privateChatList = new ArrayList<String>();

        privateChatAdapter = new PrivateChatAdapter(getContext(), privateChatList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_private_chat_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(privateChatAdapter);
        fetchChats();
    }

    private void fetchChats(){
        privateChatList = (ArrayList)dbAdapter.getFriendListForPrivateChats();
        for(String userName : privateChatList){
            privateChatAdapter.add(userName);
        }
    }


}
