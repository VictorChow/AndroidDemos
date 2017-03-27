package pers.victor.androiddemos.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Victor on 2017/3/27. (ง •̀_•́)ง
 */

public class CopyAssetsFile {
    /**
     * @param myContext
     * @param assetsName 要复制的文件名
     * @param savePath   要保存的路径
     * @param saveName   复制后的文件名
     *                   testCopy(Context context)是一个测试例子。
     */

    public static void copy(Context myContext, String assetsName, String savePath, String saveName) {
        String filename = savePath + "/" + saveName;

        File dir = new File(savePath);
        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdir();
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets().open(assetsName);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
