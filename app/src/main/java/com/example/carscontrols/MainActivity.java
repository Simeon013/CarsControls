package com.example.carscontrols;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.example.carscontrols.activity.LoginActivity;
import com.example.carscontrols.activity.ProfileActivity;
import com.example.carscontrols.admin_activity.AdminProfileActivity;
import com.example.carscontrols.helper.SQLiteHandler;
import com.example.carscontrols.helper.SessionManager;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends Activity {

    private TextView textView;
    private ImageView imageView;
    private ActionMenuItemView menuHistory;
    private ActionMenuItemView menuProfile;
    private ActionMenuItemView menuSettings;
    private EditText imma;
    private Button verification;
    private File photoFile;
    ProgressDialog progressDialog;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File directory = new File(getFilesDir(), "photos");
        if (!directory.exists()) {
            directory.mkdir();
        }

        photoFile = new File(directory, "photo.jpg");
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        imma = findViewById(R.id.imma);
        verification = findViewById(R.id.verif);
        menuHistory = findViewById(R.id.menuHistory);
        menuProfile = findViewById(R.id.menuProfile);
        menuSettings = findViewById(R.id.menuSettings);
        final Context context = this;

        FloatingActionButton btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> onTakePicture());

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


        menuHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //set title
                alertDialogBuilder.setTitle("Historique");

                //set dialog message
                alertDialogBuilder.setMessage("Accès à l'onglet historique");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                //create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show it
                alertDialog.show();
                /*Intent i = new Intent(getApplicationContext(),
                        HistoryActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        menuProfile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (grade.equals("admin")){
                    Intent i = new Intent(getApplicationContext(),
                            AdminProfileActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(getApplicationContext(),
                            ProfileActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        menuSettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //set title
                alertDialogBuilder.setTitle("Parametres");

                //set dialog message
                alertDialogBuilder.setMessage("Accès à l'onglet parametres");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                //create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show it
                alertDialog.show();
                /*Intent i = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                startActivity(i);
                finish();*/
            }
        });


        verification.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                String plaque = imma.getText().toString().trim();
                String mal = "BF 5392";
                String bon = "AT 3562";
                String ass = "BG 0062";
                String vt = "BX 3477";
                String Message ="";

                if (!plaque.isEmpty()) {
                    if (plaque.equals(mal)) {
                        Message = "Le véhicule n'est pas en règle.\nAssurance expirée.\nVisite technique expirée";
                    } else if (plaque.equals(bon)) {
                        Message = "Le véhicule est en règle.";
                    } else if (plaque.equals(ass)) {
                        Message = "Le véhicule n'est pas en règle.\nAssurance expirée.";
                            }  else if (plaque.equals(vt)) {
                        Message = "Le véhicule n'est pas en règle.\nVisite technique expirée.";
                                    }
                } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                        "SVP!!! Veuillez entrer une valeur.", Toast.LENGTH_LONG)
                                .show();
                    }


                progressDialog = new ProgressDialog(MainActivity.this);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                progressDialog.setMessage("Recherche...Veuillez patientez...");
                progressDialog.show();
                progressDialog.cancel();

                //set title
                alertDialogBuilder.setTitle("Vérification");

                //set dialog message
                alertDialogBuilder.setMessage(Message);
                //alertDialogBuilder.setMessage(finalMessage);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                //create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                //show it
                alertDialog.show();

                /*if (imma.equals("AT 3562")) {
                    Intent i = new Intent(getApplicationContext(),
                            ResultatValideActivity.class);
                    startActivity(i);
                    finish();
                }else if (imma.equals("BT 3562")){
                    Intent i = new Intent(getApplicationContext(),
                            ResultatInvalideActivity.class);
                    startActivity(i);
                    finish();
                }*/
            }
        });
    }

    private void onTakePicture() {
        Uri uri = FileProvider.getUriForFile(this, "it.polocorese.ocrcamera.fileprovider", photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            //resizeBitmap(bitmap,50);

            if (bitmap != null) {

                /* Rotate the image if necessary: */

                try {
                    ExifInterface exif = new ExifInterface(photoFile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    int rotation = 0;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotation = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotation = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotation = 270;
                            break;
                    }
                    if (rotation > 0) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(rotation);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                } catch (IOException ignored) {}

                ImageView ivPhoto = findViewById(R.id.photo);
                ivPhoto.setImageBitmap(bitmap);

                /*
                 * Text recognition:
                 */

                TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
                Frame frameImage = new Frame.Builder().setBitmap(bitmap).build();

                SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frameImage);
                String stringImageText = "";
                for (int i = 0; i < textBlockSparseArray.size(); i++)
                {
                    TextBlock textBlock = textBlockSparseArray.get(textBlockSparseArray.keyAt(i));
                    stringImageText = stringImageText + " " + textBlock.getValue();
                }
                imma.setText(stringImageText);

                /* Delete file: */
                photoFile.delete();
            }
        }
    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                int targetHeight = maxLength;
                if (source.getHeight() <= targetHeight) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (targetHeight * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;
            } else {
                int targetWidth = maxLength;

                if (source.getWidth() <= targetWidth) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (targetWidth * aspectRatio);

                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                return result;

            }
        }
        catch (Exception e)
        {
            return source;
        }
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