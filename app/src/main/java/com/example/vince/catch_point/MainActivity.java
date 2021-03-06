package com.example.vince.catch_point;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String STR_LOGIN = "Login";
    private String STR_MDP = "MDP";
    private EditText textLogin;
    private EditText textMDP;
    private Button connexionButton;
    private SharedPreferences sharedPreferences;
    private ConnexionAsynk connexion;
    private TextView error;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.fichier_shared_preferences), this.MODE_PRIVATE);
        connexion = new ConnexionAsynk();

        error = (TextView) findViewById(R.id.Error);
        textLogin = (EditText) findViewById(R.id.editLogin);
        textMDP = (EditText) findViewById(R.id.editMotDePasse);
        connexionButton = (Button) findViewById(R.id.connexionButton);

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonTapped();
            }
        });

        verifieExistencePreferences();
    }

    private void verifieExistencePreferences() {
        if (sharedPreferences.getString(STR_LOGIN,"").equals("") || sharedPreferences.getString(STR_MDP,"").equals("")){
            return;
        }else{
            Toast.makeText(getApplicationContext(),"Connexion en cours. Veuillez patienter.",Toast.LENGTH_SHORT).show();
            textLogin.setText(sharedPreferences.getString(STR_LOGIN,""));
            textMDP.setText(sharedPreferences.getString(STR_MDP,""));
            buttonTapped();
        }
    }

    private void buttonTapped() {
        connexion.execute();
        connexion = new ConnexionAsynk();
    }

    private void testSiLoginMdpCorrect() {
         if (idUser.equals("null")){
            error.setText("Login ou mot de passe incorrect.");
            textMDP.setText("");
        }else{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(STR_LOGIN, textLogin.getText().toString());
            editor.putString(STR_MDP, textMDP.getText().toString());
            editor.commit();
            changePage();
        }
    }

    private void changePage(){
        Intent homeClient = new Intent(getApplicationContext(), HomeClient.class);
        finish();
        startActivity(homeClient);
    }


    private class ConnexionAsynk extends AsyncTask<String, Void, String>{
        String URL;
        @Override
        protected String doInBackground(String... strings) {
            String loginAEnvoyer = textLogin.getText().toString();
            String passwordAEnvoyer = textMDP.getText().toString();

            //A remplacer par l'adresse IP de la machine dans le réseau
            //php bin/console server:run adresse_ip_machine:port
            URL = "http://192.168.0.19:8000/connexionAndroid/"+loginAEnvoyer+"&"+passwordAEnvoyer;


            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            Response response = null;

            try{
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            try {
                JSONObject monRetour = new JSONObject(o);
                String maValue = monRetour.getString("user");
                idUser = maValue;
            } catch (JSONException e) {
                idUser = "null";
                e.printStackTrace();
            }
            testSiLoginMdpCorrect();
        }
    }
}
