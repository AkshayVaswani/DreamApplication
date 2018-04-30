package com.example.a10012032.dreamapplication.UserAuth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a10012032.dreamapplication.MainActivity;
import com.example.a10012032.dreamapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    EditText userEdit;
    EditText passEdit;
    String sUser, sPass;
    Button sign;
    Button reg;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userEdit = (EditText) findViewById(R.id.user);
        passEdit = (EditText)findViewById(R.id.pass);
        sign = (Button) findViewById(R.id.sign);
        reg =  (Button)findViewById(R.id.reg);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    signUp.USER_KEY = user.getUid();
                } else {
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sUser = userEdit.getText().toString().trim();
                sPass = passEdit.getText().toString().trim();

                if(!TextUtils.isEmpty(sUser) && !TextUtils.isEmpty(sPass))
                {

                    mAuth.signInWithEmailAndPassword(sUser, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                /*mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        checkUserValidation(dataSnapshot, sUser);

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });*/
                                mAuthListener = new FirebaseAuth.AuthStateListener() {
                                    @Override
                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if(user != null) {
                                            signUp.USER_KEY = user.getUid();
                                        }
                                    }
                                };
                                Intent in = new Intent(Login.this, Profile.class);
                                startActivity(in);
                            }else{
                                Toast.makeText(Login.this, "User Login Doesn't Exist" , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, signUp.class));
            }
        });

    }

    /*private void checkUserValidation(DataSnapshot dataSnapshot, String emailForVer) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            DataSnapshot dataUser = (DataSnapshot) iterator.next();
            if (dataUser.child("EmailUser").getValue().toString().equals(emailForVer)) {
                if (dataUser.child("isVerified").getValue().toString() != null) {
                    Intent in = new Intent(katiRoll.this, Profile.class);
                    startActivity(in);
                } else {
                    startActivity(new Intent(katiRoll.this, MenuOne.class));
                }
            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);

    }

}

