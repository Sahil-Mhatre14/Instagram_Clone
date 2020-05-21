package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("OZHkdFDOVMypLua3zdJO9bhstrS9CQrMLQid5zBb")
                // if defined
                .clientKey("YLIzRoI9J8ugjeJFI18ijOs2UvVsoJUVajAJvb9k")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
