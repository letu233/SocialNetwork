package com.example.socialnetwork;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.Adapter.CommentAdapter;
import com.example.socialnetwork.Adapter.PostAdapter;
import com.example.socialnetwork.model.Comment;
import com.example.socialnetwork.model.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private PostAdapter postAdapter;
    private Posts posts;

    
    private EditText addComment;
    private TextView post;

    private String authorId;
    private String postId;
    private String saveCurrentDate, saveCurrentTime;

    public TextView title, content, writer, time, date, count_like, count_comment;
    public Button topic;
    public ImageView like, comment;

    private DatabaseReference PostsRef;


    FirebaseUser fUser;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(CommentActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(mManager);



        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);

        recyclerView.setAdapter(commentAdapter);




        addComment = findViewById(R.id.add_comment);
        post = findViewById(R.id.post);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");
        postAdapter = new PostAdapter(this ,posts, postId);

        time = findViewById(R.id.tv_time);
        date = findViewById(R.id.tv_date);
        topic = findViewById(R.id.btn_topic);
        title = findViewById(R.id.tv_title);
        content = findViewById(R.id.tv_content);
        writer = findViewById(R.id.tv_writer);
        like = findViewById(R.id.like);
        count_like = findViewById(R.id.count_like);
        comment = findViewById(R.id.comment_2);
        count_comment = findViewById(R.id.count_cmt);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference().child("Likes").child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String countLike = snapshot.getChildrenCount() + "";
                count_like.setText(countLike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String countComment = dataSnapshot.getChildrenCount() + "";
                count_comment.setText(countComment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String toPic = dataSnapshot.child("topic").getValue(String.class);
                topic.setText(toPic);
                String tiTle =  dataSnapshot.child("title").getValue(String.class);
                title.setText(tiTle);
                String tiMe =  dataSnapshot.child("time").getValue(String.class);
                time.setText(tiMe);
                String daTe =  dataSnapshot.child("date").getValue(String.class);
                date.setText(daTe);
                String wriTer =  dataSnapshot.child("fullname").getValue(String.class);
                writer.setText(wriTer);
                String conTent =  dataSnapshot.child("content").getValue(String.class);
                content.setText(conTent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("The Read Failed ", error.getMessage());
            }
        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    Toast.makeText(CommentActivity.this, "No Comment Added", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calFordDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy", new Locale("en"));
                    saveCurrentDate = currentDate.format(calFordDate.getTime());

                    Calendar calFordTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                    saveCurrentTime = currentTime.format(calFordDate.getTime());
                    putComment();
                }
            }
        });
        getComment();

    }


    private void getComment() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);

                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("The Read Failed ", error.getMessage());
            }
        });
    }

    private void putComment() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", addComment.getText().toString());
        map.put("publisher", fUser.getUid());
        map.put("time", saveCurrentTime);
        map.put("date", saveCurrentDate);

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId)
                .push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if ( task.isSuccessful()) {
                    Toast.makeText(CommentActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}