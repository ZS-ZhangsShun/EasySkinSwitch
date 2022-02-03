package com.zs.skinswitch.theme

import android.os.Build
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import com.zs.skinswitch.resource.ResourcesManager

object SkinThemeUtils {
/*    private val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(
        R.attr.colorPrimaryDark
    )
    private val STATUSBAR_COLOR_ATTRS = intArrayOf(
        R.attr.statusBarColor, R.attr.navigationBarColor
    )*/

    /**
     * 获得theme中的属性中定义的 资源id
     * @param context
     * @param attrs
     * @return
     */
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val a: TypedArray = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = a.getResourceId(i, 0)
        }
        a.recycle()
        return resIds
    }


    fun updateStatusBarColor(activity: Activity, colorId: Int) {
        //5.0以上才能修改
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }

        activity.window.statusBarColor = ResourcesManager.getColor(colorId)
        //获得 statusBarColor 与 nanavigationBarColor (状态栏颜色)
        //当与 colorPrimaryDark  不同时 以statusBarColor为准
        /*val resIds = getResId(activity, STATUSBAR_COLOR_ATTRS)
        val statusBarColorResId = resIds[0]
        val navigationBarColor = resIds[1]

        //如果直接在style中写入固定颜色值(而不是 @color/XXX ) 获得0
        if (statusBarColorResId != 0) {
            val color: Int = ResourcesManager.getColor(statusBarColorResId)
            activity.window.statusBarColor = color
        } else {
            //获得 colorPrimaryDark
            val colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (colorPrimaryDarkResId != 0) {
                val color: Int = ResourcesManager.getColor(colorPrimaryDarkResId)
                activity.window.statusBarColor = color
            }
        }
        if (navigationBarColor != 0) {
            val color: Int = ResourcesManager.getColor(navigationBarColor)
            activity.window.navigationBarColor = color
        }*/
    }
}