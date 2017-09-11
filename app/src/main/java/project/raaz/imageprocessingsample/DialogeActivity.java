package project.raaz.imageprocessingsample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import static project.raaz.imageprocessingsample.ImageProcess.mann;

/**
 * Created by ajmal on 10/28/2016.
 */

public class DialogeActivity extends Activity {
        GridView grid1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.dialog_activity);
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        getWindow().setGravity(Gravity.BOTTOM);
        grid1 = findViewById(R.id.gird1);

        grid1.setAdapter(new Manniqune(this));
        grid1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f));
                        break;
                    case 1:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f1));
                        break;
                    case 2:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f2));
                        break;
                    case 3:
                        mann.setImageDrawable(getResources().getDrawable(R.drawable.f3));
                        break;
                }
            }
        });
    }
}
