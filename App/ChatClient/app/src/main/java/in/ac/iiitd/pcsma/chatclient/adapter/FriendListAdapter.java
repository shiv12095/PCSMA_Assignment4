package in.ac.iiitd.pcsma.chatclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.holder.FriendListViewHolder;
import in.ac.iiitd.pcsma.chatclient.item.UserProfile;

/**
 * Created by shiv on 17/4/16.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

    private ArrayList<UserProfile> userProfileList;
    private Context context;

    public FriendListAdapter(Context context, ArrayList<UserProfile> userProfileList){
        this.context = context;
        this.userProfileList = userProfileList;
    }

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list_row, null);
        FriendListViewHolder friendListViewHolder = new FriendListViewHolder(view);
        return friendListViewHolder;

    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        UserProfile userProfile = userProfileList.get(position);
        holder.getUserIdTextView().setText(userProfile.getUserId());
    }

    @Override
    public int getItemCount() {
        if(userProfileList == null) {
            return 0;
        }else{
            return userProfileList.size();
        }
    }

    public void add(UserProfile userProfile){
        userProfileList.add(userProfile);
        notifyDataSetChanged();
    }
}
