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

/**
 * Created by shiv on 1/5/16.
 */
public class PrivateChatAdapter extends RecyclerView.Adapter<FriendListViewHolder> {

    private ArrayList<String> userPrivateChatList;
    private Context context;

    @Override
    public FriendListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list_row, null);
        FriendListViewHolder friendListViewHolder = new FriendListViewHolder(view);
        return friendListViewHolder;
    }

    public PrivateChatAdapter(Context context, ArrayList<String> userPrivateChatList){
        this.context = context;
        this.userPrivateChatList = userPrivateChatList;
    }

    @Override
    public void onBindViewHolder(FriendListViewHolder holder, int position) {
        String userName = userPrivateChatList.get(position);
        final String friendId = userName;
        holder.getUserIdTextView().setText(userName);
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
        if(userPrivateChatList == null) {
            return 0;
        }else{
            return userPrivateChatList.size();
        }
    }

    public void add(String userName){
        userPrivateChatList.add(userName);
        notifyDataSetChanged();
    }
}
