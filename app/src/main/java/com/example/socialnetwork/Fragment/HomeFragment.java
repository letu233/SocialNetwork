package com.example.socialnetwork.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.Adapter.PostAdapter;
import com.example.socialnetwork.MainActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.example.socialnetwork.model.Users;
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

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private ImageView imageView;
    private TextView textView;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ImageButton AddNewPostButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, databaseReference;
    private List<Posts> list;
    private List<Users> listUser;
    private ArrayList<String> PostKey;
    private FirebaseUser firebaseUser;
    String currentUserID;
    private Button filter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        filter = view.findViewById(R.id.filter);
//        filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog();
//            }
//        });


        recyclerView = view.findViewById(R.id.recycle_postview);
        recyclerView.setHasFixedSize(true);
//        imageView = container.findViewById(R.id.like);
//        textView = container.findViewById(R.id.count_like);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        UsersRef = FirebaseDatabase.getInstance().getReference("User");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        PostKey = new ArrayList<String>();
        postAdapter = new PostAdapter(getContext(),list, PostKey);
        recyclerView.setAdapter(postAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Posts post = dataSnapshot.getValue(Posts.class);
                    String postkey = dataSnapshot.getKey();
                    PostKey.add(postkey);
                    list.add(post);

                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        UsersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Users user = dataSnapshot.getValue(Users.class);
//                    listUser.add(user);
//                    Toast.makeText(getContext(), listUser.get(1).toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
////        mAuth = FirebaseAuth.getInstance();
////
//        mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
//        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//        postList = view.findViewById(R.id.recycler_view);
//        postList.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        postList.setLayoutManager(linearLayoutManager);
//        DisplayAllUserPosts();
//        return view;
 //       return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//    public void DisplayAllUserPosts() {
//        FirebaseRecyclerAdapter<Posts, HomeFragment.PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(Posts.class, R.layout.all_post_layout, PostsViewHolder.class, PostsRef) {
//            @Override
//            protected void populateViewHolder(PostsViewHolder holder, Posts model, int i) {
//                Toast.makeText(getContext(), model.getUid(), Toast.LENGTH_SHORT).show();
//                holder.setWriter(model.getFullname());
//                holder.setTitle(model.getDate());
//                holder.setContent(model.getContent());
////                holder.setPostimage(model.getPostimage());
////                holder.setTime(model.getTime());
//            }
//        };
//        postList.setAdapter(firebaseRecyclerAdapter);
    }
    public void isLikes(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Likes").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_like_red);
                    imageView.setTag("liked");
                }else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void numLikes(final TextView likes, String postid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Likes").child(postid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void showDialog(){
//        final Dialog dialog = new Dialog(this.getContext());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottom_sheet_layout);
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialoAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }

}