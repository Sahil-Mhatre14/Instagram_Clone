package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePicturesTab extends androidx.fragment.app.Fragment implements View.OnClickListener {
    private EditText edtDescription;
    private ImageView imgShare;
    private Button btnShareImage;
    private Bitmap selectedImage;

    public SharePicturesTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_pictures_tab, container, false);
        btnShareImage = view.findViewById(R.id.btnShareImage);
        edtDescription = view.findViewById(R.id.edtDescription);
        imgShare = view.findViewById(R.id.imgShare);

        imgShare.setOnClickListener(SharePicturesTab.this);
        btnShareImage.setOnClickListener(SharePicturesTab.this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgShare:
                if(Build.VERSION.SDK_INT>=23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]
                                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                                                1000);
                }
                else {
                    getChosenImage();
                }
                break;

            case R.id.btnShareImage:
                if (selectedImage != null){
                    if (edtDescription.getText().toString().equals("")){
                        FancyToast.makeText(getContext(),"Please enter a description",Toast.LENGTH_SHORT,
                                FancyToast.CONFUSING,true).show();
                    }
                    else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("image.png",bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture",parseFile);
                        parseObject.put("imageDescription", edtDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Uploading...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){
                                    FancyToast.makeText(getContext(),"Image Uploaded",Toast.LENGTH_SHORT,
                                            FancyToast.SUCCESS,true).show();
                                } else {
                                    FancyToast.makeText(getContext(),"ERROR: " + e.getMessage(),Toast.LENGTH_SHORT,
                                            FancyToast.ERROR,true).show();
                                }
                                dialog.dismiss();
                            }
                        });

                    }
                }
                else {
                    FancyToast.makeText(getContext(),"You, must select an image",Toast.LENGTH_SHORT,
                            FancyToast.ERROR,true).show();
                }
                break;
        }
    }

    private void getChosenImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imgShare.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}
