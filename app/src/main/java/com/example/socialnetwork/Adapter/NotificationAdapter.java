package com.example.socialnetwork.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Notification;
import com.example.socialnetwork.model.Posts;
import com.example.socialnetwork.model.Users;
import com.firebase.ui.auth.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private Context context;
    private List<Notification> list;
    private String ptime = null;
    public NotificationAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notify_post_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = list.get(position);
        if (notification.getIspost()){
            getDateTime(notification.getPostid());
            holder.title.setText(notification.getText());
            holder.time.setText(ptime);
            holder.from.setText("New post");
        }else{
            getUserInfo(notification.getUid(),holder.title);
            holder.from.setText("New Activity");
            holder.content.setText(notification.getText());
            holder.time.setText("");


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, from, time, content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notify_title);
            time = itemView.findViewById(R.id.notify_time);
            from = itemView.findViewById(R.id.from);
            content = itemView.findViewById(R.id.content_notification);
        }
    }
    private void getDateTime(String postid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts")
                .child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Posts post = snapshot.getValue(Posts.class);
                ptime = post.getTime();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getUserInfo(String uid,TextView title){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                title.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

