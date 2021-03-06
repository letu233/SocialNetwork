package com.example.socialnetwork.Fragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.example.socialnetwork.MainActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

//Home Fragment ............
public class ProfileFragment extends Fragment {
    ImageView  myPost, myFav;
    TextView posts, fullname, companytv, emailtv;
    Button edit_profile, options;

    RecyclerView recyclerView;
    PostAdapter myPostAdapter;
    List<Posts> postsList;

    List<String> listMyfavs;
    RecyclerView recyclerViewFav;
    PostAdapter myPostAdapterFav;
    List<Posts> postsListFav;
    private GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, postRef;

    private ArrayList<String> PostKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        postRef = FirebaseDatabase.getInstance().getReference("Posts");

        mAuth = FirebaseAuth.getInstance();

        options = view.findViewById(R.id.options);
        myPost = view.findViewById(R.id.my_post);
        myFav = view.findViewById(R.id.my_favourit);

        posts = view.findViewById(R.id.posts);
        fullname = view.findViewById(R.id.fullname);
        companytv = view.findViewById(R.id.company);
        emailtv = view.findViewById(R.id.email);
        edit_profile = view.findViewById(R.id.edit_profile);

        PostKey = new ArrayList<String>();
        getMyPost();

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postsList = new ArrayList<>();
        myPostAdapter = new PostAdapter(getContext(), postsList, PostKey);
        recyclerView.setAdapter(myPostAdapter);


        recyclerViewFav = view.findViewById(R.id.recycle_view_fav);
        recyclerViewFav.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerFav = new LinearLayoutManager(getContext());
        linearLayoutManagerFav.setReverseLayout(true);
        linearLayoutManagerFav.setStackFromEnd(true);
        recyclerViewFav.setLayoutManager(linearLayoutManagerFav);



        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewFav.setVisibility(View.GONE);

        myPost.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_2),android.graphics.PorterDuff.Mode.MULTIPLY);
        myFav.setColorFilter(ContextCompat.getColor(getContext(), R.color.black),android.graphics.PorterDuff.Mode.MULTIPLY);

        getUserInfo();

        edit_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        options.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signout();
            }
        });

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPost.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_2),android.graphics.PorterDuff.Mode.MULTIPLY);
                myFav.setColorFilter(ContextCompat.getColor(getContext(), R.color.black),android.graphics.PorterDuff.Mode.MULTIPLY);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewFav.setVisibility(View.GONE);
            }
        });

        myFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPost.setColorFilter(ContextCompat.getColor(getContext(), R.color.black),android.graphics.PorterDuff.Mode.MULTIPLY);
                myFav.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_2),android.graphics.PorterDuff.Mode.MULTIPLY);
                postsListFav = new ArrayList<>();
                myFavourites();
                myPostAdapterFav = new PostAdapter(getContext(), postsListFav, (ArrayList<String>) listMyfavs);
                recyclerViewFav.setAdapter(myPostAdapterFav);
                recyclerView.setVisibility(View.GONE);
                recyclerViewFav.setVisibility(View.VISIBLE);

            }
        });

        getPostCount();


        return view;
    }

    private void getUserInfo(){
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    String company = ""+ds.child("company").getValue();
                    String email = ""+ds.child("email").getValue();
                    if( company.equals("null")) company = "";
                    fullname.setText(name);
                    companytv.setText(company);
                    emailtv.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void getPostKey(){
//        postRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Posts post = dataSnapshot.getValue(Posts.class);
//                    String postkey = dataSnapshot.getKey();
//                    PostKey.add(postkey);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

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
        PostKey.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Posts post = ds.getValue(Posts.class);
                    String postkey = ds.getKey();
                    if(post.getUid()!= null && firebaseUser.getUid()!= null && post.getUid().contains(firebaseUser.getUid())){
                        PostKey.add(postkey);
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
                listMyfavs.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    String postId = ds.getKey();
                    reference.child(postId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            for (DataSnapshot ds2 : snapshot2.getChildren()){
                                if (ds2.getKey().equalsIgnoreCase(firebaseUser.getUid())){
                                    listMyfavs.add(ds.getKey());
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
                postsListFav.clear();
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

    private void signout(){
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(getContext(),gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseAuth.getInstance().signOut(); // very important if you are using firebase.
                    Intent login_intent = new Intent(getContext(),Login.class);
                    login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // clear previous task (optional)
                    startActivity(login_intent);
                }
            }
        });

    }
//    @Override
//    public void onStart() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mGoogleApiClient.connect();
//        super.onStart();
//    }

}