package project.raaz.imageprocessingsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rasheed on 9/5/2017.
 */


public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        RectF oval1 = new RectF(0, 0, getWidth(), getHeight());

        canvas.drawOval(oval1, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        RectF oval2 = new RectF(250, 50, 400, 800);
        canvas.drawOval(oval2, paint);

//        paint.setColor(Color.BLUE);
//        RectF oval3 = new RectF(250, 50, 350, 400);
//        canvas.drawOval(oval3, paint);
    }

}
