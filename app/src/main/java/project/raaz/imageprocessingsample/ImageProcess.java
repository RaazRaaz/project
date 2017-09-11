package project.raaz.imageprocessingsample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static project.raaz.imageprocessingsample.HeadSet.headImage;


/**
 * Created by rasheed on 8/6/2017.
 */

public  class ImageProcess extends Activity implements View.OnTouchListener {

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private  Bitmap bmap;

//

    public  static ImageView mann, view,preDress,mann_head_1;
    public ImageScaleView iv1;
    ImageHead iv;
    FrameLayout canvas;
    RelativeLayout ldress;
    GridView grd,grd2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        Button b1 = (Button)findViewById(R.id.b1);
        Button b2 = findViewById(R.id.b2);
        mann_head_1 = findViewById(R.id.mann_head_1);
        canvas =  findViewById(R.id.canvasView);
        ldress = findViewById(R.id.layout);
        grd = findViewById(R.id.gird2);
        grd2 = findViewById(R.id.gird3);
        mann = findViewById(R.id.mann);
        view = findViewById(R.id.mann_dress);
        preDress= findViewById(R.id.preDress);
        grd.setAdapter(new Dress(this));
        grd2.setAdapter(new Manniqune(this));
        mann.setImageDrawable(getResources().getDrawable(R.drawable.f));

        mann_head_1.setImageBitmap(headImage);
//        iv1 = new ImageScaleView(ImageProcess.this);
//        iv1.setImageDrawable(getResources().getDrawable(R.drawable.face));
//        canvas.addView(iv1);

//
//        iv = new ImageHead(ImageProcess.this);
//        iv.setImageBitmap(bmap);
//        canvas.addView(iv);
        view.setOnTouchListener(this);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ldress.setVisibility(View.VISIBLE);
//                grd.setVisibility(View.INVISIBLE);
//                grd2.setVisibility(View.VISIBLE);
                preDress.setVisibility(View.INVISIBLE);


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             ldress.setVisibility(View.VISIBLE);
                grd.setVisibility(View.VISIBLE);
                grd2.setVisibility(View.INVISIBLE);
            }
        });
        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                switch (i)
                {
                    case 0:
                        view.setImageDrawable(getResources().getDrawable(R.drawable.ld1));
                        ldress.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        view.setImageDrawable(getResources().getDrawable(R.drawable.ld2));
                        ldress.setVisibility(View.INVISIBLE);
                        break;

                    case 2:
                        view.setImageDrawable(getResources().getDrawable(R.drawable.ld3));
                        ldress.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        grd2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f));
                        grd2.setVisibility(View.INVISIBLE);

                        break;
                    case 1:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f1));
                        grd2.setVisibility(View.INVISIBLE);

                        break;
                    case 2:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f2));
                        grd2.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f3));
                        grd2.setVisibility(View.INVISIBLE);

                        break;
                }
            }
        });



        Bitmap  bitmap2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap2);
        canvas2.drawColor(Color.WHITE);
        Paint rectPaint2 = new Paint();
        rectPaint2.setColor(Color.GREEN);
        canvas2.drawRect(20, 20, 180, 180, rectPaint2);
        Matrix matrix2 = new Matrix();
        float deform2 = 20f;
        float[] src2 = new float[] { 0, 0, bitmap2.getWidth(), 0, bitmap2.getWidth(), bitmap2.getHeight(), 0, bitmap2.getHeight() };
        float[] dst2 = new float[] { 0, 0, bitmap2.getWidth() - deform2, deform2, bitmap2.getWidth() - deform2, bitmap2.getHeight() - deform2, 0, bitmap2.getHeight() };
        matrix2.setPolyToPoly(src2, 0, dst2, 0, src2.length >> 1);
        Bitmap bMatrix2= Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix2, true);

        view.setImageBitmap(bMatrix2);


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);

        bmap= Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmap);
        view.draw(canvas);


        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s=x * x + y * y;
        return (float)Math.sqrt(s);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}



