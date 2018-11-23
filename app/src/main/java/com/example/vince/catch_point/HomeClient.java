package com.example.vince.catch_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeClient extends AppCompatActivity {
    private TextView viewLogin;
    private Button deconnxion;
    private SharedPreferences sharedPreferences;
    private String STR_LOGIN = "Login";
    private String STR_MDP = "MDP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_client);

        viewLogin = (TextView) findViewById(R.id.afficheLogin);
        deconnxion = (Button) findViewById(R.id.deconnexionButton);
        deconnxion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deconnxionClient();
            }
        });
        sharedPreferences = getSharedPreferences(getString(R.string.fichier_shared_preferences), this.MODE_PRIVATE);
        viewLogin.setText("Connecté avec le login : " + sharedPreferences.getString(STR_LOGIN,""));
    }

    private void deconnxionClient() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STR_LOGIN,"");
        editor.putString(STR_MDP,"");
        editor.commit();
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(mainActivity);
    }
}
