package pers.victor.androiddemos.util;

import android.content.Context;
import android.widget.Toast;

import pers.victor.androiddemos.AndroidDemos;

/**
 * Created by Victor on 2015-9-6.
 */
public class ShowToast {
    private static final Context context = AndroidDemos.getInstance();
    private static Toast toast;

    public static void shortToast(Object text) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, String.valueOf(text), Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(text));
        }
        toast.show();
    }
}
