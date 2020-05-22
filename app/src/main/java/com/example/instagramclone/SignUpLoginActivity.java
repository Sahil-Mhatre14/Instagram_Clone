package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity {
    private EditText edtUsernameLogin, edtUsernameSignUp, edtPassowordLogin
             ,edtPasswordSignUp;
    private Button btnLogin, btnSignUp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        edtUsernameSignUp = findViewById(R.id.edtUsernameSignUp);
        edtUsernameLogin = findViewById(R.id.edtUsernameLogin);
        edtPassowordLogin = findViewById(R.id.edtPasswordLogin);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseUser appUser = new ParseUser();
                appUser.setUsername(edtUsernameSignUp.getText().toString());
                appUser.setPassword(edtPasswordSignUp.getText().toString());
                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(SignUpLoginActivity.this, appUser.get("username") + " is signed up successfully" , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        }
                        else {
                            FancyToast.makeText(SignUpLoginActivity.this, "Sign Up Unsuccessful", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                    }
                });
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(edtUsernameLogin.getText().toString(),
                        edtPassowordLogin.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e== null){
                                    FancyToast.makeText(SignUpLoginActivity.this, user.get("username") + " is logged in successfully" , FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                    Intent intent = new Intent(SignUpLoginActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    FancyToast.makeText(SignUpLoginActivity.this, e.getMessage() , FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                            }
                        });
            }
        });


    }
}
