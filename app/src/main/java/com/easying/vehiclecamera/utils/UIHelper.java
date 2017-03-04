package com.easying.vehiclecamera.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import com.easying.vehiclecamera.R;

/**
 * Created by think on 2016/11/4.
 */

public class UIHelper {

    public static float width; // 屏幕宽
    public static float height; // 屏幕高
    public static float density; // 屏幕密度

    private static float xScale; // X方向上的缩放比例
    private static float yScale; // Y方向上的缩放比例
    private static Context context;

    /**
     * @param context 上下文
     * @param width   模型图的宽
     * @param height  模型图的高
     */
    public static void init(Context context, float width, float height) {
        UIHelper.context = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        UIHelper.width = dm.widthPixels;
        UIHelper.height = dm.heightPixels;
        UIHelper.density = dm.density;
        UIHelper.xScale = UIHelper.width / width;
        UIHelper.yScale = UIHelper.height / height;
    }

    /**
     * 设置View隐藏
     *
     * @param v
     * @param gone
     */
    public static void setViewGone(View v, boolean gone) {
        if (gone) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置View隐藏(保留空间)
     *
     * @param v
     * @param invisible
     */
    public static void setViewInvisible(View v, boolean invisible) {
        if (invisible) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Turn dp to px according to resolution
     */
    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dpValue * dm.density + 0.5f);
    }

    /**
     * Turn px(pixcel) to dp according to resolution
     */
    public static int px2dip(Context context, float pxValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (pxValue / dm.density + 0.5f);
    }

    /**
     * 取转换后的宽
     */
    public static int getTranslateWidth(int width) {
        if (width < 0) {
            return -1;
        }
        return (int) (UIHelper.xScale * width) + 1;
    }

    /**
     * 取转换后的高
     */
    public static int getTranslateHeight(int height) {
        if (height < 0) {
            return -1;
        }
        return (int) (UIHelper.yScale * height) + 1;
    }

    /**
     * 设置原始大小
     */
    public static void setSizeOriginal(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width > 0) {
            layoutParams.width = width;
        }
        if (height > 0) {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置View大小
     */
    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width > 0) {
            layoutParams.width = getTranslateWidth(width);
        }
        if (height > 0) {
            layoutParams.height = getTranslateHeight(height);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 得到View的大小
     *
     * @param v
     * @param listener
     */
    public static void getViewSize(final View v, final OnSizeListener listener) {
        v.post(new Runnable() {

            @Override
            public void run() {
                if (listener != null) {
                    listener.onSize(v.getWidth(), v.getHeight());
                }
            }
        });
    }

    public interface OnSizeListener {
        void onSize(int width, int height);
    }

    /**
     * get accent color
     *
     * @return
     */
    public static int getColorPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /**
     * 以水平方向的比例来设置大小
     */
    public static void setSizeHorizontal(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (width > 0) {
            layoutParams.width = getTranslateWidth(width);
        }
        if (height > 0) {
            layoutParams.height = getTranslateWidth(height);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 以垂直方向的比例来设置大小
     */
    public static void setSizeVertical(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width > 0) {
            layoutParams.width = getTranslateHeight(width);
        }
        if (height > 0) {
            layoutParams.height = getTranslateHeight(height);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置边距
     */
    public static void setMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        int tem = getTranslateWidth(left);
        if (tem > 0) {
            layoutParams.leftMargin = tem;
        }
        tem = getTranslateHeight(top);
        if (tem > 0) {
            layoutParams.topMargin = tem;
        }
        tem = getTranslateWidth(right);
        if (tem > 0) {
            layoutParams.rightMargin = tem;
        }
        tem = getTranslateHeight(bottom);
        if (tem > 0) {
            layoutParams.bottomMargin = tem;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 以水平方向的比例来设置边距
     */
    public static void setMarginHorizontal(View view, int left, int top, int right,
                                           int bottom) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        int tem = getTranslateWidth(left);
        if (tem > 0) {
            layoutParams.leftMargin = tem;
        }
        tem = getTranslateWidth(top);
        if (tem > 0) {
            layoutParams.topMargin = tem;
        }
        tem = getTranslateWidth(right);
        if (tem > 0) {
            layoutParams.rightMargin = tem;
        }
        tem = getTranslateWidth(bottom);
        if (tem > 0) {
            layoutParams.bottomMargin = tem;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 以垂直方向的比例来设置边距
     */
    public static void setMarginVertical(View view, int left, int top, int right,
                                         int bottom) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        int tem = getTranslateHeight(left);
        if (tem > 0) {
            layoutParams.leftMargin = tem;
        }
        tem = getTranslateHeight(top);
        if (tem > 0) {
            layoutParams.topMargin = tem;
        }
        tem = getTranslateHeight(right);
        if (tem > 0) {
            layoutParams.rightMargin = tem;
        }
        tem = getTranslateHeight(bottom);
        if (tem > 0) {
            layoutParams.bottomMargin = tem;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置内边距
     */
    public static void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(getTranslateWidth(left), getTranslateHeight(top),
                getTranslateWidth(right), getTranslateHeight(bottom));
    }

    /**
     * 以水平方向的比例来设置内边距
     */
    public static void setPaddingHorizontal(View view, int left, int top, int right,
                                            int bottom) {
        view.setPadding(getTranslateWidth(left), getTranslateWidth(top),
                getTranslateWidth(right), getTranslateWidth(bottom));
    }

    /**
     * 以垂直方向的比例来设置内边距
     */
    public static void setPaddingVertical(View view, int left, int top, int right,
                                          int bottom) {
        view.setPadding(getTranslateHeight(left), getTranslateHeight(top),
                getTranslateHeight(right), getTranslateHeight(bottom));
    }

    /**
     * 取系统菜单的高度
     */
    public static int getSystemMenuHeight() {
        if (UIHelper.height > 600) {
            return 50;
        } else {
            return 42;
        }
    }

    /**
     * 状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}