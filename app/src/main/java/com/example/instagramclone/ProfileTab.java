package com.example.instagramclone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    EditText edtProfileName, edtBio, edtHobbies, edtProfession,
            edtFavSport;
    Button btnUpdateInfo;

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

        final ParseUser parseUser = ParseUser.getCurrentUser();

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

        return view;
    }
}
