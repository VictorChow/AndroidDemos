package demos.view.tetris;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Victor on 2017/3/13. (ง •̀_•́)ง
 */

public class Tetris {
    private Color color;
    private TetrisShape shape;

    private Tetris() {
    }

    public static Tetris create() {
        Tetris tetris = new Tetris();
        int shapeIndex = new Random().nextInt(7);
        tetris.setShape(TetrisShape.get(shapeIndex));
        return tetris;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public TetrisShape getShape() {
        return shape;
    }

    public void setShape(TetrisShape shape) {
        this.shape = shape;
    }
}
