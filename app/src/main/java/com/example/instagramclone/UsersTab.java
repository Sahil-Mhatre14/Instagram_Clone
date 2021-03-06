package com.example.instagramclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private Bitmap userDP;
    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        listView = view.findViewById(R.id.listView);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList );
        final TextView txtLoadingUsers = view.findViewById(R.id.txtLoadingUsers);
        listView.setOnItemClickListener(UsersTab.this);
        listView.setOnItemLongClickListener(UsersTab.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null) {
                    if(users.size() > 1) {
                        for (ParseUser user : users ){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsers.animate().alpha(0);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UsersPosts.class);
        intent.putExtra("username", arrayList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null){
                    final PrettyDialog pDialog = new PrettyDialog(getContext());
                    pDialog
                            .setTitle(user.getUsername() + "'s Info")
                            .setMessage("Bio: " + user.get("bio") + "\n"
                                        + "Profession: " + user.get("profession") + "\n"
                                        + "Hobbies: " + user.get("hobbies") + "\n"
                                        + "Favourite Sport: " + user.get("favouriteSport"))
                            .addButton(
                                    "Cancel",
                                    R.color.pdlg_color_white,
                                    R.color.pdlg_color_red,
                                    new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            pDialog.dismiss();
                                        }
                                    }
                            ).setIcon(R.drawable.person)
                            .show();
                }
            }
        });
        return true;
    }
}
