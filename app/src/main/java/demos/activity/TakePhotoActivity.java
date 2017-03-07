package demos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.victor.androiddemos.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakePhotoActivity extends Activity implements View.OnClickListener {
    private Button bntTakePic;
    private Button bntEnter;
    private Button bntCancel;
    private SurfaceView surfaceView;
    private FrameLayout fraShadeTop;
    private FrameLayout fraShadeBottom;
    private Camera camera;
    private Camera.Parameters parameters = null;
    private WindowManager mWindowManager;
    private int windowWidth;//获取手机屏幕宽度
    private int windowHeight;//获取手机屏幕高度
    private String savePath = "/aaaaaaaaaa/";
    private Bundle bundle = null;// 声明一个Bundle对象，用来存储数据
    private int IS_TOOK = 0;//是否已经拍照 ,0为否

    /**
     * 检验是否有SD卡
     *
     * @true or false
     */
    public static boolean isHaveSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    /**
     * 通过文件地址获取文件的bitmap
     *
     * @param path
     * @return
     * @throws IOException
     */

    public static Bitmap getBitmapByPath(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        init();
    }

    private void init() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager.getDefaultDisplay().getHeight();
        fraShadeTop = (FrameLayout) findViewById(R.id.fra_shade_top);
        fraShadeBottom = (FrameLayout) findViewById(R.id.fra_shade_bottom);
        RelativeLayout.LayoutParams topParams = (RelativeLayout.LayoutParams) fraShadeTop.getLayoutParams();
        topParams.width = windowWidth;
        topParams.height = (windowHeight - windowWidth) / 2;
        fraShadeTop.setLayoutParams(topParams);
        fraShadeTop.getBackground().setAlpha(200);

        RelativeLayout.LayoutParams bottomParams = (RelativeLayout.LayoutParams) fraShadeBottom.getLayoutParams();
        bottomParams.width = windowWidth;
        bottomParams.height = (windowHeight - windowWidth) / 2;
        fraShadeBottom.setLayoutParams(bottomParams);
        fraShadeBottom.getBackground().setAlpha(200);

        //按钮
        bntTakePic = (Button) findViewById(R.id.bnt_takepicture);
        bntEnter = (Button) findViewById(R.id.bnt_enter);
        bntCancel = (Button) findViewById(R.id.bnt_cancel);

        bntTakePic.setVisibility(View.VISIBLE);
        bntEnter.setVisibility(View.INVISIBLE);
        bntCancel.setVisibility(View.INVISIBLE);
        bntTakePic.setOnClickListener(this);
        bntEnter.setOnClickListener(this);
        bntCancel.setOnClickListener(this);

        //照相机预览的空间
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(150, 150); // 设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数

    }

    /**
     * 三个按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bnt_takepicture:
                // 拍照
                if (camera != null) {
                    camera.takePicture(null, null, new MyPictureCallback());
                }
                break;

            case R.id.bnt_enter:
                if (bundle == null) {
                    Toast.makeText(getApplicationContext(), "bundle null",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (isHaveSDCard())
                            saveToSDCard(bundle.getByteArray("bytes"));
                        else
                            saveToRoot(bundle.getByteArray("bytes"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
                break;
            case R.id.bnt_cancel:
                bntTakePic.setVisibility(View.VISIBLE);
                bntCancel.setVisibility(View.INVISIBLE);
                bntEnter.setVisibility(View.INVISIBLE);
                if (camera != null) {
                    IS_TOOK = 0;
                    camera.startPreview();
                }
                break;
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data
     * @throws IOException
     */
    public void saveToSDCard(byte[] data) throws IOException {
        System.out.println("saveToSDCard");

        //剪切为正方形
        Bitmap b = byteToBitmap(data);
//        Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, windowWidth, windowHeight);
        Bitmap bitmap = Bitmap.createScaledBitmap(b, windowHeight, windowWidth, true);
        //生成文件
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory()
                + savePath);
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();


        //   out.close();
        //  outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流
        Intent intent = new Intent();
        intent.putExtra("path", Environment.getExternalStorageDirectory() + savePath + filename);
        setResult(1, intent);
    }

    /**
     *
     */
    public void saveToRoot(byte[] data) throws IOException {
        //剪切为正方形
        Bitmap b = byteToBitmap(data);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, windowWidth, windowWidth);
        //生成文件
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getRootDirectory()
                + savePath);
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();


        //   out.close();
        //  outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流
        Intent intent = new Intent();
        intent.putExtra("path", Environment.getExternalStorageDirectory() + savePath + filename);
        setResult(1, intent);
    }

    /**
     * 把图片byte流编程bitmap
     *
     * @param data
     * @return
     */
    private Bitmap byteToBitmap(byte[] data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                break;
            }
            i += 1;
        }
        return b;

    }

    /**
     * 物理按键事件
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA: // 按下拍照按钮
                if (camera != null && event.getRepeatCount() == 0) {
                    // 拍照
                    //注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
                    //，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
                    camera.takePicture(null, null, new MyPictureCallback());
                }
            case KeyEvent.KEYCODE_BACK:
                if (IS_TOOK == 0)
                    finish();
                else {
                    //	camera.startPreview();
                    bntCancel.performClick();
                    return false;
                }

                break;

        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重构照相类
     *
     * @author
     */
    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            System.out.println("onPictureTaken");
            try {
                bundle = new Bundle();
                bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换

                //     saveToSDCard(data); // 保存图片到sd卡中
//                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                bntTakePic.setVisibility(View.INVISIBLE);
                bntCancel.setVisibility(View.VISIBLE);
                bntEnter.setVisibility(View.VISIBLE);
                //  camera.startPreview(); // 拍完照后，重新开始预览
                IS_TOOK = 1;

                //照完自动保存
                bntEnter.callOnClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 重构相机照相回调类
     *
     * @author pc
     */
    private final class SurfaceCallback implements SurfaceHolder.Callback {

        @SuppressWarnings("deprecation")
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            System.out.println("surfaceChanged");

            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(80); // 设置照片质量
            //  camera.setParameters(parameters);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            System.out.println("surfaceCreated");

            try {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                int cameraCount = Camera.getNumberOfCameras(); // get cameras number
                for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                    Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                        camera = Camera.open(camIdx);
                    }
                }
//                camera = Camera.open(); // 打开摄像头
//                自动对焦后拍照

                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(TakePhotoActivity.this));
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        camera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, final Camera camera) {
                                System.out.println("onAutoFocus");
                                camera.takePicture(null, null, new MyPictureCallback());
                            }
                        });
                    }
                });
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            System.out.println("surfaceDestroyed");

            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }


}