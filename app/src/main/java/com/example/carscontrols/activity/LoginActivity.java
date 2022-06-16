package com.example.carscontrols.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.carscontrols.AdminActivity;
import com.example.carscontrols.MainActivity;
import com.example.carscontrols.R;
import com.example.carscontrols.app.AppConfig;
import com.example.carscontrols.app.AppController;
import com.example.carscontrols.helper.SQLiteHandler;
import com.example.carscontrols.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputMatricule;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputMatricule = findViewById(R.id.matricule);
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
//        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String matricule = inputMatricule.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!matricule.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(matricule, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "SVP!!! Veuillez entrer une valeur.", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
/*
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
*/

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String matricule, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, response -> {
                    Log.d(TAG, "Login Response: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Now store the user in SQLite
                            //String uid = jObj.getString("uid");

                            String matricule1 = jObj.getString("matricule");

                            JSONObject user = jObj.getJSONObject("user");
                            //String matricule1 = user.getString("matricule");
                            String nom = user.getString("nom");
                            String prenom = user.getString("prenom");
                            String age = user.getString("age");
                            String telephone = user.getString("telephone");
                            String grade = user.getString("grade");


                            // Inserting row in users table
                            db.addUser(matricule1, nom, prenom, age, telephone, grade);

                            if (grade.equals("admin")){
                                // Launch admin activity
                                Intent intent = new Intent(LoginActivity.this,
                                        AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (grade.equals("user")){
                                // Launch main activity
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // Error in login. Get the error message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Matricule non enregistré!!!", Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricule", matricule);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
