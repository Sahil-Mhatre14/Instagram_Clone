package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity {
    EditText edtName, edtKickSpeed, edtKickPower;
    Button btnSave, btnGetAll;
    TextView textView;
    private String allKickBoxers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtName = findViewById(R.id.edtName);
        edtKickPower = findViewById(R.id.edtKickPower);
        edtKickSpeed = findViewById(R.id.edtKickSpeed);
        btnSave = findViewById(R.id.btnSave);
        btnGetAll = findViewById(R.id.btnGetAll);
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("8bmZCsSrCb", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(object!=null && e==null){
                            textView.setText(object.get("Name") + "\n"
                                            + "KickSpeed: " + object.get("Kick_Speed")
                                            + "\n" + "KickPower: " + object.get("kick_power"));
                        }
                    }
                });
            }
        });

        btnGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allKickBoxers = "";
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
                queryAll.whereGreaterThan("Kick_Speed",500);
                queryAll.setLimit(1);
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null){
                            if(objects.size() > 0){
                                for(ParseObject kickBoxer : objects) {
                                    allKickBoxers = allKickBoxers + "Name: " + kickBoxer.get("Name")
                                                    + " Kick Speed: " + kickBoxer.get("Kick_Speed")
                                                    + " Kick Power: " + kickBoxer.get("kick_power") + "\n";
                                }
                                FancyToast.makeText(SignUp.this, allKickBoxers, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                            }
                        }
                        else {
                            FancyToast.makeText(SignUp.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                    }
                });
            }
        });

    }
    public void btnSaveIsTapped(View view){
//        ParseObject boxer = new ParseObject("Boxer");
//        boxer.put("punch_speed",250);
//        boxer.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null){
//                    Toast.makeText(SignUp.this,"Save Successful",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(SignUp.this,"Save Unsuccessful",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
       try {
           final ParseObject kickBoxer = new ParseObject("KickBoxer");
           kickBoxer.put("Name", edtName.getText().toString());
           kickBoxer.put("Kick_Speed", Integer.parseInt(edtKickSpeed.getText().toString()));
           kickBoxer.put("kick_power", Integer.parseInt(edtKickPower.getText().toString()));
           kickBoxer.saveInBackground(new SaveCallback() {
               @Override
               public void done(ParseException e) {
                   if (e == null) {
                       FancyToast.makeText(SignUp.this, kickBoxer.get("Name") + " saved successfully", FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                   } else {
                       FancyToast.makeText(SignUp.this, "Save Unsuccessful" ,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                   }
               }
           });
       } catch (Exception e){
           FancyToast.makeText(this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
       }
    }
}
