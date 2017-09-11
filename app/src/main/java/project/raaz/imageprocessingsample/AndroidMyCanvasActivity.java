package project.raaz.imageprocessingsample;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by rasheed on 9/5/2017.
 */


public class AndroidMyCanvasActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
}