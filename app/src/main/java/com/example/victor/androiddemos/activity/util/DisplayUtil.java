package com.example.victor.androiddemos.activity.util;

import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.example.victor.androiddemos.activity.AndroidDemos;


/**
 * Created by Victor on 2015-8-30.
 */
public class DisplayUtil {

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */

    public static int dp2px(float dipValue) {
        return (int) (dipValue * AndroidDemos.screenDensity + 0.5f);
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */

    public static int sp2px(float spValue) {
        return (int) (spValue * AndroidDemos.scaledDensity + 0.5f);
    }

    /**
     * 改变字符串中个别字体大小
     *
     * @param text
     * @param textSize 要改变的字体大小（sp）
     * @param isDip    字体单位是否是dip
     * @param start    开始位置
     * @param end      结束位置 （前包后不包）
     * @return
     */
    public static SpannableString changeTextSize(String text, int textSize, boolean isDip,
                                                 int start, int end) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new AbsoluteSizeSpan(textSize, isDip), start, end,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    /**
     * 改变字符串中个别字体加粗
     *
     * @param text
     * @param start 开始位置
     * @param end   结束位置 （前包后不包）
     * @return
     */
    public static SpannableString changeTextBold(String text, int start, int end) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return sp;
    }

    /**
     * 改变字符串中个别字体颜色
     *
     * @param text
     * @param colorId 颜色id
     * @param start   开始位置
     * @param end     结束位置 （前包后不包）
     * @return
     */
    public static SpannableString changeTextColor(String text, int colorId, int start, int end) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(AndroidDemos.getInstance(), colorId)), start, end,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return sp;
    }

    /**
     * 设置TextView下划线
     */
    public static void setTextViewUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textView.getPaint().setAntiAlias(true);
    }

    /**
     * 设置TextView删除线
     */
    public static void setTextViewDeleteLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        textView.getPaint().setAntiAlias(true);
    }

}
