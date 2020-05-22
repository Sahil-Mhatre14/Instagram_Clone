package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmailLogin, edtPasswordLogin;
    private Button btnLoginLogin, btnSignUpLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnSignUpLogin = findViewById(R.id.btnSignUpLogin);

        btnSignUpLogin.setOnClickListener(LoginActivity.this);
        btnLoginLogin.setOnClickListener(LoginActivity.this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLoginLogin:
                ParseUser.logInInBackground(edtEmailLogin.getText().toString(),
                        edtPasswordLogin.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e == null){
                                    FancyToast.makeText(LoginActivity.this,  ParseUser.getCurrentUser().get("username")+ " is logged in successfully",
                                            Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                } else{
                                    FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                            Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                            }
                        });
                break;

            case R.id.btnSignUpLogin:
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
                break;
        }
    }
}
