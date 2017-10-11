package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.process;

import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities.CameraActivity;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.TapListener;

/**
 * Created by Sudarshan on 11/25/2015.
 */
public class CameraProcess {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera.PictureCallback jpegCallback;
    private CameraActivity mainActivity;
    private int cameraTypeInUse;
    private Handler myHandler;

    public CameraProcess(CameraActivity mA){
        this.mainActivity = mA;
        myHandler = new Handler(Looper.myLooper());

        surfaceView = (SurfaceView) mainActivity.findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(new TapListener() {
            @Override
            public void onDoubleClick(View v) {
                try{
                    switchCamera();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSingleClick(View v) {}
        });
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mainActivity.getGestureDetector().onTouchEvent(event);
                return false;
            }
        });

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(mainActivity);


        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera c) {
                try {
                    Toast.makeText(mainActivity, "Please Wait... Processing your Picture!",Toast.LENGTH_LONG).show();
                    camera.stopPreview();
                    DressCheckApplication.CMP.setRecentImage(BitmapFactory.decodeByteArray(data, 0, data.length));
                    if(cameraTypeInUse == Camera.CameraInfo.CAMERA_FACING_BACK){
                        DressCheckApplication.CMP.setRecentImage(BitmapHelperTools.rotateBitmap(DressCheckApplication.CMP.getRecentImage(), 180));
                        DressCheckApplication.CMP.setRecentImage(BitmapHelperTools.flipBitmap(DressCheckApplication.CMP.getRecentImage(), true));
                        DressCheckApplication.CMP.setRecentImage(BitmapHelperTools.getResizedBitmap(DressCheckApplication.CMP.getRecentImage(),
                                mainActivity.getWindowManager().getDefaultDisplay().getHeight()/2));
                    }
                    mainActivity.displayTakenImage();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * The method calls the camera to capture the view, and passes
     * it the jpegCallBack, which manages the storing of the bitmap.
     *
     * @param v The view that is to be captured.
     */
    public void captureImage(View v){
        camera.takePicture(null, null, jpegCallback);
    }

    /**
     * 
     */
    public void initializeAppropriateCamera(){
        camera = getFrontFacingCamera();
        if (camera == null)
            camera = getBackFacingCamera();
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {}

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {}
    }

    public void switchCamera() throws Exception{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        camera.stopPreview();
                        camera.release();
                    }catch(Exception e){e.printStackTrace();}

                    if (cameraTypeInUse == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        try {
                            camera = getBackFacingCamera();
                        }catch(Exception e){
                            camera = getFrontFacingCamera();
                        }
                    } else if (cameraTypeInUse == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        try {
                            camera = getFrontFacingCamera();
                        }catch(Exception e){
                            camera = getBackFacingCamera();
                        }
                    } else {
                        Toast.makeText(mainActivity, "Sorry! No camera is available. Please close the app and open it again", Toast.LENGTH_LONG).show();
                    }

                    camera.setDisplayOrientation(90);
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                }catch(Exception e){
                    Toast.makeText(mainActivity, "Sorry! No camera is available. Please close the app and open it again", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        myHandler.post(runnable);

    }

    private Camera getFrontFacingCamera(){
        int cameraCount;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cam = Camera.open( camIdx );
            }
        }
        cameraTypeInUse = Camera.CameraInfo.CAMERA_FACING_FRONT;
        return cam;
    }

    private Camera getBackFacingCamera(){
        int cameraCount;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cam = Camera.open( camIdx );
            }
        }

        cameraTypeInUse = Camera.CameraInfo.CAMERA_FACING_BACK;
        return cam;
    }

    public Camera getCamera(){
        return camera;
    }

    public SurfaceHolder getSurfaceHolder(){
        return surfaceHolder;
    }

    public void setCamera(Camera c){
        this.camera = c;
    }
}
