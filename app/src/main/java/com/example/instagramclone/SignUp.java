package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnLogin, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");
        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
        }

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(SignUp.this);
        btnLogin.setOnClickListener(SignUp.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
            final ParseUser appUser = new ParseUser();
            appUser.setUsername(edtUsername.getText().toString());
            appUser.setEmail(edtEmail.getText().toString());
            appUser.setPassword(edtPassword.getText().toString());
            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        FancyToast.makeText(SignUp.this, appUser.get("username") + " is signed up successfully",
                                Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else{
                        FancyToast.makeText(SignUp.this, e.getMessage(),
                                Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
            break;

            case R.id.btnLogin:
                Intent intent = new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
                break;
        }

    }
}
