package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        final String receivedUsername = receivedIntentObject.getStringExtra("username");
        FancyToast.makeText(UsersPosts.this,
                "Viewing " + receivedUsername + "'s posts", Toast.LENGTH_SHORT,
                    FancyToast.SUCCESS, true).show();

        setTitle(receivedUsername + "'s posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery("Photo");
        parseQuery.whereEqualTo("username",receivedUsername);
        parseQuery.orderByDescending("createdAt");
        final ProgressDialog dialog = new ProgressDialog(UsersPosts.this);
        dialog.setMessage("Loading...");
        dialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject post : objects) {
                        final TextView postDes = new TextView(UsersPosts.this);
                        postDes.setText(post.get("imageDescription") + "");

                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImgView = new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000);
                                    params.setMargins(5,5,5,5);
                                    postImgView.setLayoutParams(params);
                                    postImgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImgView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams desParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    desParams.setMargins(5,5,5,15);
                                    postDes.setLayoutParams(desParams);
                                    postDes.setTextSize(30f);
                                    postDes.setGravity(Gravity.CENTER);
                                    postDes.setTextColor(Color.WHITE);
                                    postDes.setBackgroundColor(Color.RED);

                                    linearLayout.addView(postImgView);
                                    linearLayout.addView(postDes);
                                }
                            }
                        });

                    }

                } else {
                    FancyToast.makeText(UsersPosts.this,
                            receivedUsername + " does not have any posts yet" , Toast.LENGTH_SHORT,
                            FancyToast.INFO, true).show();
                    finish();
                }
                dialog.dismiss();
            }
        });

    }


}
