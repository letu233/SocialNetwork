package com.example.socialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.content.Context;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.Fragment.HomeFragment;
import com.example.socialnetwork.model.Posts;
import com.example.socialnetwork.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.socialnetwork.Fragment.HomeFragment;
import com.example.socialnetwork.Fragment.NotifyFragment;
import com.example.socialnetwork.Fragment.ProfileFragment;
import com.example.socialnetwork.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

//    private NavigationView navigationView;
//    private DrawerLayout drawerLayout;
//    private RecyclerView postList;
//    private ImageButton AddNewPostButton;
//    private FirebaseAuth mAuth;
//    private DatabaseReference UsersRef, PostsRef;
//    private Fragment fragment;
//    String currentUserID;
//    @SuppressLint("WrongViewCast")
//    @Override
//
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//        UsersRef = FirebaseDatabase.getInstance().getReference().child("User");
//        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//
//        drawerLayout=(DrawerLayout) findViewById(R.id.drawable_layout);
//        navigationView=(NavigationView) findViewById(R.id.bottom_navigation);
//
//        postList = (RecyclerView) findViewById(R.id.all_user_post_list);
//        postList.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        postList.setLayoutManager(linearLayoutManager);
//
////        View navView = navigationView.inflateHeaderView(R.layout.navgation_header);
//
//        AddNewPostButton = (ImageButton) findViewById(R.id.add_new_post_button);
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item)
//            {
//                UserMenuSelector(item);
//                return false;
//            }
//        });
//
//
//
//        AddNewPostButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SendUserToPostActivity();
//            }
//        });
//
//        DisplayAllUserPosts();
//    }
//    private void DisplayAllUserPosts() {
////        FirebaseRecyclerOptions<Posts> options =
////                new FirebaseRecyclerOptions.Builder<Posts>()
////                        .setQuery(PostsRef, Posts.class)
////                        .build();
////        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
////            @Override
////            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {
////                holder.setFullname(model.getFullname());
////                holder.setDate(model.getDate());
////                holder.setDescription(model.getDescription());
////                holder.setPostimage(model.getPostimage());
////                holder.setTime(model.getTime());
////            }
////
////            @NonNull
////            @Override
////            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////                View view = LayoutInflater.from(parent.getContext())
////                        .inflate(R.layout.all_post_layout, parent, false);
////                return new PostsViewHolder (view);
////            }
////        };
//        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(Posts.class, R.layout.all_post_layout, PostsViewHolder.class, PostsRef) {
//            @Override
//            protected void populateViewHolder(PostsViewHolder holder, Posts model, int i) {
//                holder.setFullname(model.getFullname());
//                holder.setDate(model.getDate());
//                holder.setDescription(model.getContent());
//                holder.setPostimage(model.getPostimage());
//                holder.setTime(model.getTime());
//            }
//        };
//        postList.setAdapter(firebaseRecyclerAdapter);
//    }
//
//    public static class PostsViewHolder extends RecyclerView.ViewHolder{
//        View mView;
//        public PostsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView = itemView;
//        }
//        public void setFullname(String fullname){
//            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
//            username.setText(fullname);
//        }
//        public void setPostimage( String image){
//            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
//            Picasso.get().load(image).into(post_image);
//        }
//        public void setTime(String time){
//            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
//            PostTime.setText(time);
//
//        }
//        public void setDate(String Date){
//            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
//            PostDate.setText(Date);
//        }
//        public void setDescription(String Des){
//            TextView PostDes = (TextView) mView.findViewById(R.id.post_description);
//            PostDes.setText(Des);
//        }
//    }
//
//    private void SendUserToPostActivity() {
//        Intent addNewPostIntent = new Intent(MainActivity.this, PostActivity.class);
//        startActivity(addNewPostIntent);
//    }
//
//    private void UserMenuSelector(MenuItem item){
//        Fragment selected = null;
//        switch (item.getItemId()){
//            case R.id.nav_home:
//                selected = new HomeFragment();
//                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_search:
//                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_add:
//                SendUserToPostActivity();
//                break;
//            case R.id.nav_notify:
//                Toast.makeText(this, "friends", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_profile:
//                Toast.makeText(this, "find friends", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_setting:
//                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_messages:
//                Toast.makeText(this, "messages", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_logout:
//                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        if (selected != null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.bottom,
//                    selected).commit();
//        }
//    }
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private RecyclerView postList;
    private ImageButton AddNewPostButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;
    String currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        Users user = new Users(signInAccount.getDisplayName(),"null",signInAccount.getEmail(),uid);
        checkUserExist(user);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);
        //mAuth = FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        //sersRef = FirebaseDatabase.getInstance().getReference().child("User");
        //PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //postList = (RecyclerView) findViewById(R.id.all_user_post_list);
        //postList.setHasFixedSize(true);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        //postList.setLayoutManager(linearLayoutManager);
//        DisplayAllUserPosts();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

//    private void DisplayAllUserPosts() {
//        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(Posts.class, R.layout.all_post_layout, PostsViewHolder.class, PostsRef) {
//            @Override
//            protected void populateViewHolder(PostsViewHolder holder, Posts model, int i) {
//                holder.setFullname(model.getFullname());
//                holder.setDate(model.getDate());
//                holder.setDescription(model.getContent());
//                holder.setPostimage(model.getPostimage());
//                holder.setTime(model.getTime());
//            }
//        };
//        postList.setAdapter(firebaseRecyclerAdapter);
//    }
//    public static class PostsViewHolder extends RecyclerView.ViewHolder{
//        View mView;
//        public PostsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mView = itemView;
//        }
//        public void setFullname(String fullname){
//            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
//            username.setText(fullname);
//        }
//        public void setPostimage( String image){
//            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
//            Picasso.get().load(image).into(post_image);
//        }
//        public void setTime(String time){
//            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
//            PostTime.setText(time);
//
//        }
//        public void setDate(String Date){
//            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
//            PostDate.setText(Date);
//        }
//        public void setDescription(String Des){
//            TextView PostDes = (TextView) mView.findViewById(R.id.post_description);
//            PostDes.setText(Des);
//        }
//    }

    private void SendUserToPostActivity() {
        Intent addNewPostIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(addNewPostIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selected = new HomeFragment();
                    break;


                case R.id.nav_search:
                    selected = new SearchFragment();
                    break;

                case R.id.nav_add:
                    SendUserToPostActivity();
                    break;

                case R.id.nav_profile:
                    selected = new ProfileFragment();
                    break;


                case R.id.nav_notify:
                    selected = new NotifyFragment();
                    break;


            }
            if (selected != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selected).commit();
            }

            return true;
        }
    };

    private void checkUserExist(Users user){
        Users u = new Users(user.getName(),"null",user.getEmail());
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = dr.child("User");
        Query query = userRef.orderByChild("email").equalTo(user.getEmail());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    //create new user
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(user.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Toast.makeText(Home.this,"Registered!!!",Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(Home.this,"Failed!!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this,"User exist!!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }
}