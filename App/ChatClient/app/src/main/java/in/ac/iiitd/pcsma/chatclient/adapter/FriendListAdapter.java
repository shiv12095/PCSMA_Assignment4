package in.ac.iiitd.pcsma.chatclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.ac.iiitd.pcsma.chatclient.R;
import in.ac.iiitd.pcsma.chatclient.activity.TextActivity;
import in.ac.iiitd.pcsma.chatclient.commons.Constants;
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
        final String friendId = userProfile.getUserId();
        holder.getUserIdTextView().setText(userProfile.getUserId());
        holder.getUserIdTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TextActivity.class);
                intent.putExtra(Constants.FRIEND_ID, friendId);
                context.startActivity(intent);
            }
        });
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
