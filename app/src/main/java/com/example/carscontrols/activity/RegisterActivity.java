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
import com.example.carscontrols.R;
import com.example.carscontrols.app.AppConfig;
import com.example.carscontrols.app.AppController;
import com.example.carscontrols.helper.SQLiteHandler;
import com.example.carscontrols.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnBack;
    private EditText inputMatricule;
    private EditText inputNom;
    private EditText inputPrenom;
    private EditText inputAge;
    private EditText inputTelephone;
    private EditText inputPassword;
    private EditText inputConf_password;
    private EditText inputGrade;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputMatricule = (EditText) findViewById(R.id.matricule);
        inputNom = (EditText) findViewById(R.id.nom);
        inputPrenom = (EditText) findViewById(R.id.prenom);
        inputAge = (EditText) findViewById(R.id.age);
        inputTelephone = (EditText) findViewById(R.id.telephone);
        inputPassword = (EditText) findViewById(R.id.password);
        inputConf_password = (EditText) findViewById(R.id.conf_password);
        inputGrade = (EditText) findViewById(R.id.grade);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack = (Button) findViewById(R.id.btnBack);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
/*        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String matricule = inputMatricule.getText().toString().trim();
                String nom = inputNom.getText().toString().trim();
                String prenom = inputPrenom.getText().toString().trim();
                String age = inputAge.getText().toString().trim();
                String telephone = inputTelephone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String conf_password = inputConf_password.getText().toString().trim();
                String grade = inputGrade.getText().toString().trim();

                if (!matricule.isEmpty() && !nom.isEmpty() && !prenom.isEmpty() && !age.isEmpty()
                        && !telephone.isEmpty() && !password.isEmpty() && !conf_password.isEmpty() && !grade.isEmpty()) {
                    if (conf_password.equals(password)){
                        registerUser(matricule, nom, prenom, age, telephone, password, grade);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                        "Les deux mot de passe diffèrent !!!", Toast.LENGTH_LONG)
                                .show();
                        }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Veuillez remplir toute les cases", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Admin Screen
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        AdminActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String matricule, final String nom, final String prenom, final String age,
                              final String telephone, final String password, final String grade) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Ajout...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, response -> {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            // User successfully stored in MySQL
                            // Now store the user in sqlite
                            String matricule1 = jObj.getString("matricule");

                            JSONObject user = jObj.getJSONObject("user");
                            //String matricule = user.getString("matricule");
                            String nom1 = user.getString("nom");
                            String prenom1 = user.getString("prenom");
                            String age1 = user.getString("age");
                            String telephone1 = user.getString("telephone");
                            String grade1 = user.getString("grade");

                            // Inserting row in users table
                            db.addUser(matricule1, nom1, prenom1, age1, telephone1, grade1);

                            Toast.makeText(getApplicationContext(), "L'utilisateur a été créé avec succès!!!", Toast.LENGTH_LONG).show();
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();

                            // Launch Register activity
                            Intent i = new Intent(getApplicationContext(),
                                    RegisterActivity.class);
                            startActivity(i);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Eurreur : Ajout d'utilisateur " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("matricule", matricule);
                params.put("nom", nom);
                params.put("prenom", prenom);
                params.put("age", age);
                params.put("telephone", telephone);
                params.put("password", password);
                params.put("grade", grade);

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
