package com.example.socialnetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialnetwork.model.Posts;
import com.example.socialnetwork.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    ImageView closeIv;
    TextView saveTv;
    EditText fullnameEt, companyEt;

    FirebaseUser firebaseUser;
    String postId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        closeIv = findViewById(R.id.close);
        saveTv = findViewById(R.id.save);
        fullnameEt = findViewById(R.id.fullname);
        companyEt = findViewById(R.id.company);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                fullnameEt.setText(user.getName());
                companyEt.setText(user.getCompany());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        closeIv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fullnameEt.getText().toString(), companyEt.getText().toString());
                updateUserNameOfPost(fullnameEt.getText().toString());

            }
        });
    }

    private void updateProfile(String fullname, String company) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", fullname);
        hashMap.put("company", company);
        reference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfileActivity.this, "Successfully!!! Click button X to go back", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateUserNameOfPost(String fullname){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullname", fullname);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Posts post = snapshot.getValue(Posts.class);
                    String postId = firebaseUser.getUid()+post.getDate() + post.getTime();
                    if (post.getUid()!= null && firebaseUser.getUid()!= null && post.getUid().contains(firebaseUser.getUid())){
                        reference.child(postId).updateChildren(hashMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}