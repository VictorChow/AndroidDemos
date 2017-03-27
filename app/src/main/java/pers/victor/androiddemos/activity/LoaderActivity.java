package pers.victor.androiddemos.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.File;

import dalvik.system.DexClassLoader;
import pers.victor.androiddemos.R;
import pers.victor.androiddemos.util.CopyAssetsFile;

public class LoaderActivity extends ToolbarActivity {
    private DexClassLoader dexClassLoader;
    private Resources resources;
    private AssetManager assetManager;

    @Override
    public int bindLayout() {
        return R.layout.activity_loader;
    }

    @Override
    public void initView() {
        String apkPath = getFilesDir() + "/plugin.apk";
        String className = "pers.victor.ndkdemo.TestFragment";

        File plugin = new File(apkPath);
        if (!plugin.exists()) {
            CopyAssetsFile.copy(this, "plugin.apk", getFilesDir().toString(), "plugin.apk");
        }

        try {
            dexClassLoader = new DexClassLoader(apkPath, this.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(), null, super.getClassLoader());
            assetManager = AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(assetManager, apkPath);
            resources = new Resources(assetManager, this.getResources().getDisplayMetrics(), this.getResources().getConfiguration());

            Fragment fragment = (Fragment) dexClassLoader.loadClass(className).newInstance();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.rl_container_loader, fragment);
            fragmentTransaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ClassLoader getClassLoader() {
        return dexClassLoader == null ? super.getClassLoader() : dexClassLoader;
    }

    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }


    public AssetManager getAssetManager() {
        return assetManager == null ? super.getAssets() : assetManager;
    }

}
