package com.ranezgmail.shubham16.livestatusupdate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ranezgmail.shubham16.livestatusupdate.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialoge;
    private DatabaseReference mUserDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //assign variables
        mNameEditText = findViewById(R.id.nameditTextRegister);
        mEmailEditText = findViewById(R.id.emaileditTextRegister);
        mPasswordEditText = findViewById(R.id.passwordeditTextRegister);
        mRegisterButton = findViewById(R.id.registerButton);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUserDB = FirebaseDatabase.getInstance().getReference().child("User");

        mDialoge = new ProgressDialog(this);

        //listen to the button click
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString().trim();
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(name)){
                    //name cannot be empty
                    mDialoge.dismiss();
                    showAlertDialog("Error","Name cannot be empty");
                }else if (TextUtils.isEmpty(email)){
                    //email cannot be empty
                    mDialoge.dismiss();
                    showAlertDialog("Error","Email cannot be empty");
                }else if (TextUtils.isEmpty(password)){
                    //password cannot be empty
                    mDialoge.dismiss();
                    showAlertDialog("Error","Password cannot be empty");
                }else{
                    //proceed, all data a is available
                    //sign up with firebase
                    registerUserToFirebase(email, password, name);
                }

            }
        });

    }
    private  void showAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void registerUserToFirebase(String email, String password, final String name){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //error register user
                    showAlertDialog("Error!", task.getException().getMessage());
                }else{
                    //success
                    final FirebaseUser currentUser = task.getResult().getUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    currentUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            User newUser = new User(currentUser.getDisplayName(), currentUser.getEmail(), "", currentUser.getUid());
                            mUserDB.child(currentUser.getUid()).setValue(newUser);
                            //take user to home
                            finish();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        }
                    });

                }
            }
        });
    }



}

