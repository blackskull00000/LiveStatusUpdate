package com.ranezgmail.shubham16.livestatusupdate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //assign var
        mEmailEditText = findViewById(R.id.emaileditTextLogin);
        mPasswordEditText = findViewById(R.id.passwordeditTextLogin);
        mLoginButton = findViewById(R.id.loginButton);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        //listen to login button click
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setMessage("Please wait...");
                mDialog.show();
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    //name cannot be empty
                    mDialog.dismiss();
                    showAlertDialog("Error","email cannot be empty");
                }else if (TextUtils.isEmpty(password)){
                    //password cannot be empty
                    mDialog.dismiss();
                    showAlertDialog("Error","Password cannot be empty");
                }else{
                    //proceed, all data a is available
                    //logig
                    loginViaFirebase(email, password);
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

    private void loginViaFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(!task.isSuccessful()){
                    showAlertDialog("Error",task.getException().getMessage());
                }else{
                    finish();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.createAccountMenu:
                mAuth.signOut();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
