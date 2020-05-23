package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

        edtPasswordLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLoginLogin);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            // transitionToSocialMediaActivity();
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLoginLogin:
                if (edtEmailLogin.getText().toString().equals("")
                    || edtPasswordLogin.getText().toString().equals("") ){
                    FancyToast.makeText(LoginActivity.this, "Please fill all the fields",
                            Toast.LENGTH_LONG, FancyToast.INFO, true).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging In");
                    ParseUser.logInInBackground(edtEmailLogin.getText().toString(),
                            edtPasswordLogin.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        FancyToast.makeText(LoginActivity.this, ParseUser.getCurrentUser().get("username") + " is logged in successfully",
                                                Toast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                        transitionToSocialMediaActivity();
                                    } else {
                                        FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                                Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                }
                break;

            case R.id.btnSignUpLogin:
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
                break;
        }
    }

    public void rootLayoutIsTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void transitionToSocialMediaActivity(){
        Intent intent = new Intent(LoginActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
