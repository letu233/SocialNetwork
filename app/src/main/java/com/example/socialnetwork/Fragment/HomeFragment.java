package com.example.socialnetwork.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.MainActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private RecyclerView postList;
    private ImageButton AddNewPostButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;
    String currentUserID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        mAuth = FirebaseAuth.getInstance();
//
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postList = view.findViewById(R.id.recycler_view);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        DisplayAllUserPosts();
        return view;
    }
    public void DisplayAllUserPosts() {
        FirebaseRecyclerAdapter<Posts, HomeFragment.PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(Posts.class, R.layout.all_post_layout, PostsViewHolder.class, PostsRef) {
            @Override
            protected void populateViewHolder(PostsViewHolder holder, Posts model, int i) {
                Toast.makeText(getContext(), model.getUid(), Toast.LENGTH_SHORT).show();
                holder.setWriter(model.getFullname());
                holder.setTitle(model.getDate());
                holder.setContent(model.getContent());
//                holder.setPostimage(model.getPostimage());
//                holder.setTime(model.getTime());
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
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
//    }
}}