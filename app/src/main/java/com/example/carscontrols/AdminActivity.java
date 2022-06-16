package com.example.carscontrols;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.carscontrols.activity.LoginActivity;
import com.example.carscontrols.activity.RegisterActivity;
import com.example.carscontrols.admin_activity.AdminProfileActivity;
import com.example.carscontrols.helper.SQLiteHandler;
import com.example.carscontrols.helper.SessionManager;

import java.util.HashMap;

public class AdminActivity extends Activity {

    private SQLiteHandler db;
    private SessionManager session;

    private CardView addUser;
    private ImageButton btnLogout;
    private ImageButton backB;
    private TextView admin_name;
    private TextView admin_matricule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
        String grade = user.get("grade");
        String nomprenom = nom + " " + prenom;

        admin_name = findViewById(R.id.admin_name);
        admin_matricule = findViewById(R.id.admin_matricule);

        addUser = findViewById(R.id.addCard);

        btnLogout = findViewById(R.id.logOutB);
        backB = findViewById(R.id.backB);

        // Displaying the user details on the screen
        admin_matricule.setText(matricule);
        admin_name.setText(nomprenom);

        backB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        AdminProfileActivity.class);
                startActivity(i);
                finish();
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });



        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent i = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivity(i);
        finish();
    }
}