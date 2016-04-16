package in.ac.iiitd.pcsma.chatclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iiitd.pcsma.chatclient.R;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_private_chat, container, false);
    }
}
