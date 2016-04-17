package in.ac.iiitd.pcsma.chatclient.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import in.ac.iiitd.pcsma.chatclient.R;

/**
 * Created by shiv on 17/4/16.
 */
public class FriendListViewHolder extends RecyclerView.ViewHolder {

    private TextView userIdTextView;

    public FriendListViewHolder(View view){
        super(view);
        this.userIdTextView = (TextView)view.findViewById(R.id.friend_list_user_id_text_view);
    }

    public TextView getUserIdTextView() {
        return userIdTextView;
    }
}
