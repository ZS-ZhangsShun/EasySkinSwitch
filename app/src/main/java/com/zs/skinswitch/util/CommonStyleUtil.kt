package com.zs.skinswitch.util

import android.app.Activity
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.zs.easy.common.constants.EasyVariable
import com.zs.skinswitch.constants.ZSConstants

object CommonStyleUtil {
    /**
     * 设置状态栏透明的风格
     */
    fun setStatusTransparentStyle(window: Window, isTextBlack: Boolean) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //android 6.0及以上支持改变状态栏字体颜色
            if (isTextBlack) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            //这两句加上后 在我的Dialog里面 布局延伸到了状态栏此时要加一定的marginTop值来调整
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //android 5.0及以上支持改变状态栏为透明状态
            //顶部状态栏透明 这个一定要设置才生效 此处放在6.0以上才执行是因为
            //如果5.0执行了 则透明了 如果背景又是白色 就啥都看不见了 所以5.0要想实现效果需要UI的配合
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 设置黑白风格 取消黑白风格 -- 设置颜色矩阵的饱和度偏移量为默认值  为 0
     * 调用之后当前Activity可立刻生效
     * @param activity
     */
    fun setGrayStyle(activity: Activity) {
        val isGray: Boolean =
            EasyVariable.spCommon.getBoolean(ZSConstants.SP_GRAY_STYLE, false)
        if (isGray) {
            //设置灰度风格
            val paint = Paint()
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            paint.colorFilter = ColorMatrixColorFilter(cm)
            activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
        }
    }

    /**
     * 取消黑白风格 -- 设置颜色矩阵的饱和度偏移量为默认值  1
     *
     * @param activity
     */
    fun cancelGrayStyle(activity: Activity) {
        //设置灰度风格
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(1f)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }
}