package pers.victor.androiddemos.view.tetris;

/**
 * Created by Victor on 2017/3/13. (ง •̀_•́)ง
 */

public enum TetrisShape {
    L, LR, S, SR, I, O, T;

    public static TetrisShape get(int index) {
        TetrisShape tetrisShape;
        switch (index) {
            case 0:
                tetrisShape = TetrisShape.L;
                break;
            case 1:
                tetrisShape = TetrisShape.LR;
                break;
            case 2:
                tetrisShape = TetrisShape.S;
                break;
            case 3:
                tetrisShape = TetrisShape.SR;
                break;
            case 4:
                tetrisShape = TetrisShape.I;
                break;
            case 5:
                tetrisShape = TetrisShape.O;
                break;
            case 6:
                tetrisShape = TetrisShape.T;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return tetrisShape;
    }

}
