package com.example.socialnetwork.Fragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialnetwork.Adapter.PostAdapter;
import com.example.socialnetwork.EditProfileActivity;
import com.example.socialnetwork.Login;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    ImageView options, myPost, myFav;
    TextView posts, fullname, companytv, emailtv;
    Button edit_profile;

    RecyclerView recyclerView;
    PostAdapter myPostAdapter;
    List<Posts> postsList;

    List<String> listMyfavs;
    RecyclerView recyclerViewFav;
    PostAdapter myPostAdapterFav;
    List<Posts> postsListFav;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        mAuth = FirebaseAuth.getInstance();

        options = view.findViewById(R.id.options);
        myPost = view.findViewById(R.id.my_post);
        myFav = view.findViewById(R.id.my_favourit);

        posts = view.findViewById(R.id.posts);
        fullname = view.findViewById(R.id.fullname);
        companytv = view.findViewById(R.id.company);
        emailtv = view.findViewById(R.id.email);
        edit_profile = view.findViewById(R.id.edit_profile);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postsList = new ArrayList<>();
        myPostAdapter = new PostAdapter(getContext(), postsList);
        recyclerView.setAdapter(myPostAdapter);


        recyclerViewFav = view.findViewById(R.id.recycle_view_fav);
        recyclerViewFav.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerFav = new LinearLayoutManager(getContext());
        linearLayoutManagerFav.setReverseLayout(true);
        linearLayoutManagerFav.setStackFromEnd(true);
        recyclerViewFav.setLayoutManager(linearLayoutManagerFav);
        postsListFav = new ArrayList<>();
        myPostAdapterFav = new PostAdapter(getContext(), postsListFav);
        recyclerViewFav.setAdapter(myPostAdapterFav);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewFav.setVisibility(View.GONE);


        myFavourites();
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    String company = ""+ds.child("company").getValue();
                    String email = ""+ds.child("email").getValue();

                    fullname.setText(name);
                    companytv.setText(company);
                    emailtv.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        options.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewFav.setVisibility(View.GONE);
            }
        });

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewFav.setVisibility(View.VISIBLE);

            }
        });

        getPostCount();
        getMyPost();


        return view;
    }
    private void getPostCount(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Posts post = snapshot.getValue(Posts.class);
                    if (post.getUid()!= null && firebaseUser.getUid()!= null && post.getUid().contains(firebaseUser.getUid())){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMyPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Posts post = ds.getValue(Posts.class);
                    if(post.getUid()!= null && firebaseUser.getUid()!= null && post.getUid().contains(firebaseUser.getUid())){
                        postsList.add(post);
                    }
                }
                myPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myFavourites(){
        listMyfavs = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Likes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String postId = ds.getKey();
//                    System.out.println("postIdddddddddddd : "+postId);
                    reference.child(postId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot ds2 : snapshot2.getChildren()){
                                if (ds2.getKey().equalsIgnoreCase(firebaseUser.getUid())){
                                    listMyfavs.add(ds.getKey());
//                                    System.out.println("iddddd : "+ds.getKey());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                readMyFav();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readMyFav() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    Posts post = ds.getValue(Posts.class);
                    for (String id : listMyfavs){
                        if(ds.getKey().equalsIgnoreCase(id)){
                            postsListFav.add(post);
                        }
                    }

                }
                myPostAdapterFav.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}