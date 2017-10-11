package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Direction;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.BitmapHelperTools;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process.CameraProcess;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SwipeListener;


public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    public static final int GET_FROM_GALLERY = 3;

    private CameraProcess cameraProcess;
    private GestureDetector gestureDetector;
    private ImageView uploadImage;
    private ImageButton clickPhoto, switchCamera;
    private SurfaceView surfaceView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Take a Picture!");
        setContentView(R.layout.activity_camera);
        cameraProcess = new CameraProcess(this);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        Button help = (Button) findViewById(R.id.userHelper1);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogAlert();
            }
        });

        clickPhoto = (ImageButton) findViewById(R.id.clickPicture);
        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                cameraProcess.captureImage(v);
                switchCamera.setAlpha(0);
                uploadImage.setAlpha(0);

                LayoutInflater li = LayoutInflater.from(CameraActivity.this);
                View promptsView = li.inflate(R.layout.layout_please_wait, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setCancelable(true);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            }
        });

        switchCamera = (ImageButton) findViewById(R.id.switchButton);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    cameraProcess.switchCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        uploadImage = (ImageView)findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });

        gestureDetector = new GestureDetector(new SwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if(direction.equals(Direction.down)){
                    Intent i = new Intent(getApplicationContext(),WelcomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.open_translate_yaxis_ud, R.anim.close_scale);
                    finish();
                }
                if(direction.equals(Direction.up)   ||  direction.equals(Direction.down))
                    return false;
                return true;
            }
        });

            if(DressCheckApplication.tutorialCount_camera > 0){
                createDialogAlert();
                DressCheckApplication.tutorialCount_camera--;
            }

    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        cameraProcess.refreshCamera();

    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            cameraProcess.initializeAppropriateCamera();
            Camera.Parameters param = cameraProcess.getCamera().getParameters();
            cameraProcess.getCamera().setParameters(param);
            cameraProcess.getCamera().setPreviewDisplay(cameraProcess.getSurfaceHolder());
            cameraProcess.getCamera().setDisplayOrientation(90);
            cameraProcess.getCamera().startPreview();
        }catch (Exception e){
            Toast.makeText(CameraActivity.this, "EC002: Unable to Load Camera", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if(cameraProcess == null || cameraProcess.getCamera() == null){
            return;
        }

        cameraProcess.getCamera().stopPreview();
        cameraProcess.getCamera().release();
        cameraProcess.setCamera(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                Toast.makeText(this, "Please Wait... Processing your Picture!",Toast.LENGTH_LONG).show();
                bitmap = BitmapHelperTools.flipBitmap(bitmap, true);
                bitmap = BitmapHelperTools.rotateBitmap(bitmap, 270);

                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;

                bitmap = BitmapHelperTools.getResizedBitmap(bitmap, width);

                DressCheckApplication.CMP.setRecentImage(bitmap);
                displayTakenImage();


            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Sorry... We are not able to process your image at the moment.",Toast.LENGTH_LONG).show();
                e.printStackTrace();

            } catch (IOException e) {
                Toast.makeText(this, "Sorry... We are not able to process your picture at the moment",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    public final void displayTakenImage(){
        Intent i = new Intent(getApplicationContext(),PictureDisplayActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    public GestureDetector getGestureDetector(){
        return gestureDetector;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onDestroy(){
        cameraProcess = null;
        System.gc();
        super.onDestroy();
    }

    private void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(CameraActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_camera, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
