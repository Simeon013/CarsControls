package com.example.carscontrols;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File directory = new File(getFilesDir(), "photos");
        if (!directory.exists()) {
            directory.mkdir();
        }

        photoFile = new File(directory, "photo.jpg");
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        Button takePicture = findViewById(R.id.button);
        takePicture.setOnClickListener(v -> onTakePicture());
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

                ImageView ivPhoto = findViewById(R.id.imageView);
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
                textView.setText(stringImageText);

                /* Delete file: */
                photoFile.delete();
            }
        }
    }
}