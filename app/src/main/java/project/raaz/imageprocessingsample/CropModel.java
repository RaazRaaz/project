package project.raaz.imageprocessingsample;

/**
 * Created by rasheed on 8/16/2017.
 */
public class CropModel {
    float x;
    float y;

    public CropModel(float y, float x) {
        this.y = y;
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
