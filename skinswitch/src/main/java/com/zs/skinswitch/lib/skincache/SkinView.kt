package com.zs.skinswitch.lib.skincache

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.zs.skinswitch.lib.interfaces.SkinViewSupportInter
import com.zs.skinswitch.lib.resource.ResourcesManager

/**
 * 每一个支持换肤的view 都包装组成一个SkinView
 */
class SkinView {
    lateinit var view: View
    lateinit var skinPairs: MutableList<SkinPair>

    constructor(view: View, skinPairs: MutableList<SkinPair>) {
        this.view = view
        this.skinPairs = skinPairs
    }

    /**
     * 对当前view执行换肤操作
     */
    fun applySkin(){
        //如果当前view是自定义view 则 执行其自己的除了常规的background、textColor等属性的换肤逻辑
        if (view is SkinViewSupportInter) {
            (view as SkinViewSupportInter).applySkin()
        }
        for ((attributeName, resId) in skinPairs) {
            var left: Drawable? = null
            var top: Drawable? = null
            var right: Drawable? = null
            var bottom: Drawable? = null
            when (attributeName) {
                "background" -> {
                    val background: Any = ResourcesManager.getBackground(resId)
                    //背景可能是 @color 也可能是 @drawable
                    if (background is Int) {
                        view.setBackgroundColor(background)
                    } else {
                        ViewCompat.setBackground(view, background as Drawable)
                    }
                }
                "src" -> {
                    val background: Any = ResourcesManager.getBackground(resId)
                    if (background is Int) {
                        (view as ImageView).setImageDrawable(ColorDrawable((background as Int?)!!))
                    } else {
                        (view as ImageView).setImageDrawable(background as Drawable?)
                    }
                }
                "textColor" -> (view as TextView).setTextColor(
                    ResourcesManager.getColorStateList(
                        resId
                    )
                )
                "drawableLeft" -> left = ResourcesManager.getDrawable(resId)
                "drawableTop" -> top = ResourcesManager.getDrawable(resId)
                "drawableRight" -> right = ResourcesManager.getDrawable(resId)
                "drawableBottom" -> bottom = ResourcesManager.getDrawable(resId)
                else -> {
                }
            }
            if (null != left || null != right || null != top || null != bottom) {
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    left, top, right,
                    bottom
                )
            }
        }
    }
}