package com.example.socialnetwork.Adapter;

import android.view.View;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PostAdapter {
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private RecyclerView postList;
    private ImageButton AddNewPostButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;
    String currentUserID;
    private void DisplayAllUserPosts() {

        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        FirebaseRecyclerAdapter<Posts, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, ViewHolder>(Posts.class, R.layout.all_post_layout, ViewHolder.class, PostsRef) {
            @Override
            protected void populateViewHolder(ViewHolder holder, Posts model, int i) {
                holder.setWriter(model.getFullname());
                holder.setTitle(model.getDate());
                holder.setContent(model.getContent());
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, content, writer;
        View mView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);

        }
        public void setTitle (String Title){
            TextView title = (TextView) mView.findViewById(R.id.tv_title);
            title.setText(Title);
        }
        public void setContent (String Content){
            TextView content = (TextView) mView.findViewById(R.id.tv_content);
            content.setText(Content);
        }
        public void setWriter (String Writer){
            TextView writer = (TextView) mView.findViewById(R.id.tv_writer);
            writer.setText(Writer);
        }
    }
}
