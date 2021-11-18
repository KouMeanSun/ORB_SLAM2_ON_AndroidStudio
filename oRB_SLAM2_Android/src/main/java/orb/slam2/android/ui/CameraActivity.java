package orb.slam2.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import orb.slam2.android.R;
import orb.slam2.android.nativefunc.OrbNdkHelper;
import orb.slam2.android.ui.controller.Camera2LandspaceController;
import orb.slam2.android.ui.surface.AutoFitTextureView;

public class CameraActivity extends Activity implements
        GLSurfaceView.Renderer {
    private static final String TAG = "CameraActivity";
    private Camera2LandspaceController mController;
    String vocPath, calibrationPath;
    ImageView imgDealed;
    private AutoFitTextureView mTextureView;

    private static final int INIT_FINISHED=0x00010001;
    static {
        System.loadLibrary("ORB_SLAM2_EXCUTOR");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        mTextureView = (AutoFitTextureView)this.findViewById(R.id.texture);
        vocPath = getIntent().getStringExtra("voc");
        calibrationPath = getIntent().getStringExtra("calibration");
        Log.e(TAG,"slam vocPath:"+vocPath);
        Log.e(TAG,"slam calibrationPath:"+calibrationPath);

        imgDealed = (ImageView) findViewById(R.id.img_dealed);


        vocPath = getIntent().getStringExtra("voc");
        calibrationPath = getIntent().getStringExtra("calibration");
        Log.e(TAG,"slam vocPath:"+vocPath);
        Log.e(TAG,"slam calibrationPath:"+calibrationPath);

        if (TextUtils.isEmpty(vocPath) || TextUtils.isEmpty(calibrationPath)) {
            Toast.makeText(this, "null param,return!", Toast.LENGTH_LONG)
                    .show();
//			finish();
        } else {
            Toast.makeText(this, "init has been started!",
                    Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    OrbNdkHelper.initSystemWithParameters(vocPath,
                            calibrationPath);
                    Log.e("information==========>",
                            "init has been finished!");
                    myHandler.sendEmptyMessage(INIT_FINISHED);
                }
            }).start();
        }

        mController = new Camera2LandspaceController(this,this,mTextureView);
    }

    Handler myHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_FINISHED:
                    Toast.makeText(CameraActivity.this,
                            "init has been finished!",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Bitmap tmp, resultImg;
    private double timestamp;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        //OrbNdkHelper.readShaderFile(mAssetMgr);
        OrbNdkHelper.glesInit();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        OrbNdkHelper.glesResize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        OrbNdkHelper.glesRender();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mController.onResume();
    }



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mController.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String toastMesage = new String();
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        return true;
    }

}