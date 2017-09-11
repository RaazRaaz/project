package project.raaz.imageprocessingsample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridView;

/**
 * Created by ajmal on 10/28/2016.
 */

public class DialogeActivity1 extends Activity {
        GridView grid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.dialog_activity);
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        getWindow().setGravity(Gravity.BOTTOM);
        grid1 = findViewById(R.id.gird1);
        grid1.setAdapter(new Dress(this));
    }
}