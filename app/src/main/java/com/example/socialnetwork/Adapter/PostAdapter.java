package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.MainActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public Context mContext;
    public List<Posts> mPost;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Posts> mPost){
        this.mContext = mContext;
        this.mPost = mPost;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Posts post = mPost.get(position);
        holder.time.setText(post.getTime());
        holder.date.setText(post.getDate());
        holder.topic.setText(post.getTopic());
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.writer.setText(post.getFullname());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, writer, time, date;
        public Button topic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            topic = itemView.findViewById(R.id.btn_topic);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            writer = itemView.findViewById(R.id.tv_writer);


        }
    }


}
