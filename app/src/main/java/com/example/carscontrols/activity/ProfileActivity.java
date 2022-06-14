package com.example.carscontrols.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.FileProvider;

import com.example.carscontrols.MainActivity;
import com.example.carscontrols.R;
import com.example.carscontrols.helper.SQLiteHandler;
import com.example.carscontrols.helper.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.HashMap;

public class ProfileActivity extends Activity {

    private TextView txtNom;
    private TextView txtMatricule;
    private TextView txtPrenom;
    private Button btnLogout;
    private FloatingActionButton btnCapture;
    private ActionMenuItemView menuProfile;
    private ActionMenuItemView menuSettings;
    private ActionMenuItemView menuHistory;
    private File photoFile;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        File directory = new File(getFilesDir(), "photos");
        if (!directory.exists()) {
            directory.mkdir();
        }

        photoFile = new File(directory, "photo.jpg");

        txtNom = findViewById(R.id.txtNom);
        txtMatricule = findViewById(R.id.txtMatricule);
        txtPrenom = findViewById(R.id.txtPrenom);
        btnLogout = findViewById(R.id.btnLogout);
        btnCapture = findViewById(R.id.btnCapture);
        menuHistory = findViewById(R.id.menuHistory);
        menuProfile = findViewById(R.id.menuProfile);
        menuSettings = findViewById(R.id.menuSettings);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String nom = user.get("nom");
        String prenom = user.get("prenom");
        String matricule = user.get("matricule");

        // Displaying the user details on the screen
        txtNom.setText(nom);
        txtPrenom.setText(prenom);
        txtMatricule.setText(matricule);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        btnCapture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        menuProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        menuSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });

        menuHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        HistoryActivity.class);
                startActivity(i);
                finish();
            }
        });



        /*menuInfo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        /*btnProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent i = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void onTakePicture() {
        Uri uri = FileProvider.getUriForFile(this, "it.polocorese.ocrcamera.fileprovider", photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 10);
    }
}