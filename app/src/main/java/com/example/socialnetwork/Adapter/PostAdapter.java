package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.CommentActivity;
import com.example.socialnetwork.MainActivity;
import com.example.socialnetwork.PostActivity;
import com.example.socialnetwork.R;
import com.example.socialnetwork.model.Posts;
import com.example.socialnetwork.model.Topics;
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
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    public Context mContext;
    public List<Posts> mPost;
    public Posts posts;
    public String postKey;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference PostsRef;
    private ArrayList<String> PostKey;
    public PostAdapter(Context mContext, List<Posts> mPost, ArrayList<String>Postkey){
        this.mContext = mContext;
        this.mPost = mPost;
        this.PostKey = Postkey;
    }

    public PostAdapter(Context mContext, Posts posts, String postKey) {
        this.mContext = mContext;
        this.posts = posts;
        this.postKey = postKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, parent, false);
//        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
//        PostsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                PostKey=new ArrayList<String>();
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    String postid = dataSnapshot.getKey();
//                    Toast.makeText(mContext, postid, Toast.LENGTH_SHORT).show();
//
//                   PostKey.add(postid);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String postid = PostKey.get(position);
        Posts post = mPost.get(position);
        String sort = displayContent(post.getContent());
        holder.time.setText(post.getTime());
        holder.date.setText(post.getDate());
        holder.topic.setText(post.getTopic());
        holder.title.setText(post.getTitle());
        holder.content.setText(sort);
        holder.writer.setText(post.getFullname());

//        String postid = post.getUid() + post.getDate() + post.getTime();

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", postid);
                intent.putExtra("authorId", post.getFullname());
                intent.putExtra("writerId", post.getUid());
                mContext.startActivity(intent);

            }
        });
        holder.count_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", postid);
                intent.putExtra("authorId", post.getFullname());
                mContext.startActivity(intent);
            }
        });
        getComments(postid, holder.count_comment);

        isLikes(postid, holder.like);
        numLikes(holder.count_like, postid);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(postid).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post.getUid(), postid);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(postid).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public void isLikes(String postid, final ImageView imageView){
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

    private void getComments (String postId, final TextView text ){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText( dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, writer, time, date, count_like, count_comment;
        public Button topic;
        public ImageView like, comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            topic = itemView.findViewById(R.id.btn_topic);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            writer = itemView.findViewById(R.id.tv_writer);
            like = itemView.findViewById(R.id.like);
            count_like = itemView.findViewById(R.id.count_like);
            comment = itemView.findViewById(R.id.comment);
            count_comment = itemView.findViewById(R.id.count_cmt);
        }
    }
    private String displayContent(String content){
        int count = 0;
        StringBuilder sb = new StringBuilder();
        String[] words = content.split(" ");
        for (String w: words){
            sb.append(w+ " ");
            count++;
            if (count == 10) break;
        }
        return sb.toString();
    }

    private void addNotifications(String uid, String postid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications")
                .child(uid);
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", firebaseUser.getUid());
        map.put("text", "liked your post");
        map.put("postid", postid);
        map.put("ispost", false);
        databaseReference.push().setValue(map);

    }


}