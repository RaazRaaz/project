package project.raaz.imageprocessingsample;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static project.raaz.imageprocessingsample.MainActivity.captureImage;

public class CropActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    ImageView im_crop_image_view;
    Path clipPath;
    public static Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0;
    float downy = 0;
    float tdownx = 0;
    float tdowny = 0;
    float upx = 0;
    float upy = 0;
    long lastTouchDown = 0;
    int CLICK_ACTION_THRESHHOLD = 100;
    Display display;
    Point size;
    int screen_width,screen_height;
    Button btn_ok;
    ArrayList<CropModel> cropModelArrayList;
    float smallx,smally,largex,largey;
    Paint cpaint;
    public static Bitmap temporary_bitmap;
    private ProgressDialog pDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        init();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        int cx = (screen_width - bmp.getWidth()) >> 1;
        int cy = (screen_height - bmp.getHeight()) >> 1;
        canvas.drawBitmap(bmp, cx, cy, null);
        im_crop_image_view.setImageBitmap(alteredBitmap);
        im_crop_image_view.setOnTouchListener(this);

    }

    private void init() {

        pDialog = new ProgressDialog(CropActivity.this);
        im_crop_image_view = (ImageView) findViewById(R.id.im_crop_image_view);
        cropModelArrayList = new ArrayList<>();
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

        initcanvas();
    }

    public void initcanvas() {

//        Drawable d = getResources().getDrawable(R.drawable.sample);
//        bmp = ((BitmapDrawable)d).getBitmap();
        bmp = getResizedBitmap(captureImage,1200);
        alteredBitmap = Bitmap.createBitmap(screen_width, screen_height, bmp.getConfig());
        canvas = new Canvas(alteredBitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0));

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:


                downx = event.getX();
                downy = event.getY();
                clipPath = new Path();
                clipPath.moveTo(downx, downy);
                tdownx = downx;
                tdowny = downy;
                smallx = downx;
                smally = downy;
                largex = downx;
                largey = downy;
                lastTouchDown = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                upx = event.getX();
                upy = event.getY();
                cropModelArrayList.add(new CropModel(upx, upy));
                clipPath = new Path();
                clipPath.moveTo(tdownx,tdowny);
                for(int i = 0; i<cropModelArrayList.size();i++){
                    clipPath.lineTo(cropModelArrayList.get(i).getY(),cropModelArrayList.get(i).getX());
                }
                canvas.drawPath(clipPath, paint);
                im_crop_image_view.invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD) {

                    cropModelArrayList.clear();
                    initcanvas();

                    int cx = (screen_width - bmp.getWidth()) >> 1;
                    int cy = (screen_height - bmp.getHeight()) >> 1;
                    canvas.drawBitmap(bmp, cx, cy, null);
                    im_crop_image_view.setImageBitmap(alteredBitmap);

                } else {
                    if (upx != upy) {
                        upx = event.getX();
                        upy = event.getY();


                        canvas.drawLine(downx, downy, upx, upy, paint);
                        clipPath.lineTo(upx, upy);
                        im_crop_image_view.invalidate();

                        crop();
                    }

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void crop() {

        clipPath.close();
        clipPath.setFillType(Path.FillType.INVERSE_WINDING);

        for(int i = 0; i<cropModelArrayList.size();i++){
            if(cropModelArrayList.get(i).getY()<smallx){

                smallx=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()<smally){

                smally=cropModelArrayList.get(i).getX();
            }
            if(cropModelArrayList.get(i).getY()>largex){

                largex=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()>largey){

                largey=cropModelArrayList.get(i).getX();
            }
        }

        temporary_bitmap = alteredBitmap;
        cpaint = new Paint();
        cpaint.setAntiAlias(true);
        cpaint.setColor(getResources().getColor(R.color.colorAccent));
        cpaint.setAlpha(100);
        canvas.drawPath(clipPath, cpaint);

        canvas.drawBitmap(temporary_bitmap, 0, 0, cpaint);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                save();

            default:
                break;
        }

    }


    private void save() {

        if(clipPath != null) {
            final int color = 0xff424242;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(alteredBitmap, 0, 0, paint);

            float w = largex - smallx;
            float h = largey - smally;
            alteredBitmap = Bitmap.createBitmap(alteredBitmap, (int) smallx, (int) smally, (int) w, (int) h);

            alteredBitmap = bmp;

        }else{
//            alteredBitmap = bmp;
            Toast.makeText(CropActivity.this,"please redo",Toast.LENGTH_SHORT).show();
        }
        pDialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {

                Bitmap bitmap = alteredBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
                byte[] byteArray = stream.toByteArray();
                pDialog.dismiss();

                Intent intent = new Intent(CropActivity.this, HeadSet.class);
//                intent.putExtra("image",byteArray);
                startActivity(intent);
            }
        };
        mThread.start();

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }



    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


}