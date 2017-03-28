package pers.victor.androiddemos.module;

import android.graphics.drawable.Drawable;

/**
 * Created by Victor on 2017/4/1. (ง •̀_•́)ง
 */

public class ApkModule {
    private String name;
    private String path;
    private Drawable icon;
    private String length;

    public ApkModule(String name, String path, Drawable icon, String length) {
        this.name = name;
        this.path = path;
        this.icon = icon;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
