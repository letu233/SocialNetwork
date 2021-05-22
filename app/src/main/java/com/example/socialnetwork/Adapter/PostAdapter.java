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
    private FirebaseAuth mAuth;

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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Posts post = mPost.get(position);
        String sort = displayContent(post.getContent());
        holder.time.setText(post.getTime());
        holder.date.setText(post.getDate());
        holder.topic.setText(post.getTopic());
        holder.title.setText(post.getTitle());
        holder.content.setText(sort);
        holder.writer.setText(post.getFullname());

        String postid = post.getUid() + post.getDate() + post.getTime();
        isLikes(postid, holder.like);
        numLikes(holder.count_like, postid);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(postid).child(firebaseUser.getUid()).setValue(true);
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, writer, time, date, count_like;
        public Button topic;
        public ImageView like;
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


}
