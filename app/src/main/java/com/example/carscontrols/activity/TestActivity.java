package com.example.carscontrols.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.carscontrols.R;

public class TestActivity extends Activity {
    private Context mContext;
    private Resources mResources;
    private RelativeLayout mRelativeLayout;
    private Button mBTN;
    private ImageView mImageView;
    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // Get the application context
        mContext = getApplicationContext();
        // Get the Resources
        mResources = getResources();
        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mImageView = (ImageView) findViewById(R.id.iv);
        mBTN = (Button) findViewById(R.id.btn);
        // Get the bitmap resource id
        final int bitmapResourceID =R.drawable.butterfly;
        // Set an image to ImageView
        mImageView.setImageBitmap(BitmapFactory.decodeResource(mResources, bitmapResourceID));
        // Set a click listener for Button widget
        mBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the bitmap from drawable resources
                mBitmap = BitmapFactory.decodeResource(mResources, bitmapResourceID);
                // Create a circular bitmap
                mBitmap = getCircularBitmap(mBitmap);
                // Add a border around circular bitmap
                mBitmap = addBorderToCircularBitmap(mBitmap, 15, Color.WHITE);
                // Add a shadow around circular bitmap
                mBitmap = addShadowToCircularBitmap(mBitmap, 4, Color.LTGRAY);
                // Set the ImageView image as drawable object
                mImageView.setImageBitmap(mBitmap);
            }
        });
    }
    protected Bitmap getCircularBitmap(Bitmap srcBitmap) {
        // Calculate the circular bitmap width with border
        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());
        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap (
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);
        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth-srcBitmap.getWidth())/2;
        float top = (squareBitmapWidth-srcBitmap.getHeight())/2;
        canvas.drawBitmap(srcBitmap, left, top, paint);
        // Free the native object associated with this bitmap.
        srcBitmap.recycle();
        // Return the circular bitmap
        return dstBitmap;
    }
    // Custom method to add a border around circular bitmap
    protected Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Calculate the circular bitmap width with border
        int dstBitmapWidth = srcBitmap.getWidth()+borderWidth*2;
        // Initialize a new Bitmap to make it bordered circular bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth()/2 - borderWidth / 2, // Radius
                paint // Paint
        );
        // Free the native object associated with this bitmap.
        srcBitmap.recycle();
        // Return the bordered circular bitmap
        return dstBitmap;
    }
    // Custom method to add a shadow around circular bitmap
    protected Bitmap addShadowToCircularBitmap(Bitmap srcBitmap, int shadowWidth, int shadowColor){
        // Calculate the circular bitmap width with shadow
        int dstBitmapWidth = srcBitmap.getWidth()+shadowWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth,dstBitmapWidth, Bitmap.Config.ARGB_8888);
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(srcBitmap, shadowWidth, shadowWidth, null);
        // Paint to draw circular bitmap shadow
        Paint paint = new Paint();
        paint.setColor(shadowColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(shadowWidth);
        paint.setAntiAlias(true);
        // Draw the shadow around circular bitmap
        canvas.drawCircle (
                dstBitmapWidth / 2, // cx
                dstBitmapWidth / 2, // cy
                dstBitmapWidth / 2 - shadowWidth / 2, // Radius
                paint // Paint
        );
        srcBitmap.recycle();
        return dstBitmap;
    }
}