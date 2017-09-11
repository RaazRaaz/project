package project.raaz.imageprocessingsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Raaz on 7/26/2016.
 */
public class Dress extends BaseAdapter {
    private Context ctx;
    private Integer image[]={R.drawable.ld1,R.drawable.ld2,R.drawable.ld3};
    public Dress(Context ctx){
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView img;

        if(view == null){
//            img = new ImageView(ctx);
//            img.setLayoutParams(new GridView.LayoutParams(175,175));
//            img.setScaleType(ImageView.ScaleType.CENTER);
            LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.item1, null);
            img = view.findViewById(R.id.dress);

        }else {
            img = (ImageView) view;
        }
        img.setImageResource(image[i]);
        return img;
    }
}
