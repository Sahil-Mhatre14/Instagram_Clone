package com.example.instagramclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Permission;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    EditText edtProfileName, edtBio, edtHobbies, edtProfession,
            edtFavSport;
    Button btnUpdateInfo, btnUpdateProfilePicture;
    ImageView imgProfilePicture;
    Bitmap selectedImage;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);
        btnUpdateInfo = view.findViewById(R.id.btnUpadeInfo);
        edtProfileName = view.findViewById(R.id.edtProfileName);
        edtBio = view.findViewById(R.id.edtBio);
        edtProfession = view.findViewById(R.id.edtProfession);
        edtHobbies = view.findViewById(R.id.edtHobbies);
        edtFavSport = view.findViewById(R.id.edtFavSport);
        imgProfilePicture = view.findViewById(R.id.imgProfilePicture);
        btnUpdateProfilePicture = view.findViewById(R.id.btnUpdateProfilePicture);

        final ParseUser parseUser = ParseUser.getCurrentUser();
        if(parseUser.get("profileName") != null || parseUser.get("bio") != null || parseUser.get("profession") != null
                || parseUser.get("hobbies") !=null || parseUser.get("favouriteSport") != null || parseUser.get("profilePic") != null){
            edtProfileName.setText(parseUser.get("profileName").toString());
            edtBio.setText(parseUser.get("bio").toString());
            edtProfession.setText(parseUser.get("profession").toString());
            edtHobbies.setText(parseUser.get("hobbies").toString());
            edtFavSport.setText(parseUser.get("favouriteSport").toString());
            loadUserDP();
        }
        else {
            edtProfileName.setText("");
            edtBio.setText("");
            edtProfession.setText("");
            edtHobbies.setText("");
            // setHint sets the hint and "" also sets the hint used in layout
            //so both are same
            edtFavSport.setHint("Enter Favourite Sport");
        }



        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put("profileName", edtProfileName.getText().toString());
                parseUser.put("bio", edtBio.getText().toString());
                parseUser.put("profession", edtProfession.getText().toString());
                parseUser.put("hobbies", edtHobbies.getText().toString());
                parseUser.put("favouriteSport", edtFavSport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(getContext(),"Info Updated ✅✅", Toast.LENGTH_LONG,
                                    FancyToast.INFO, true).show();
                        } else {
                            FancyToast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG,
                                    FancyToast.ERROR, true).show();
                        }
                    }
                });
            }
        });

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }
                else {
                    getChosenProfilePicture();
                }
            }
        });

        btnUpdateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage != null) {

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte bytes[] = byteArrayOutputStream.toByteArray();
                    ParseFile parseFile = new ParseFile("profilePicture.png", bytes);
                    ParseObject parseObject = new ParseObject("DisplayPicture");
                    parseObject.put("dp", parseFile);
                    parseObject.put("username",ParseUser.getCurrentUser().getUsername());
//                    parseUser.put("profilePic", parseFile);
                    ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Updating Profile Picture...");
                    dialog.show();

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(getContext(),"DP Updated ✅✅", Toast.LENGTH_LONG,
                                        FancyToast.INFO, true).show();
                            } else {
                                FancyToast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG,
                                        FancyToast.ERROR, true).show();
                            }
                        }
                    });

                    dialog.dismiss();

                }
            }
        });

        return view;
    }

    void getChosenProfilePicture() {
        Intent intent = new Intent (Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenProfilePicture();
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
                imgProfilePicture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public void loadUserDP() {
        final ParseQuery<ParseObject> parseQuery = new ParseQuery("DisplayPicture");
        parseQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject user : objects){
                        ParseFile dp = (ParseFile) user.get("dp");
                        dp.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null) {
                                    Bitmap dpBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                    imgProfilePicture.setImageBitmap(dpBitmap);
                                } else {
                                    FancyToast.makeText(getContext(),"No DP found", Toast.LENGTH_LONG,
                                            FancyToast.ERROR, true).show();
                                }
                            }
                        });
                    }

                } else {
                    FancyToast.makeText(getContext(),"No DP found", Toast.LENGTH_LONG,
                            FancyToast.ERROR, true).show();
                }
            }
        });


    }





}
