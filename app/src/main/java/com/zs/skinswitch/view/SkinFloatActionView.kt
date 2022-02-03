package com.zs.skinswitch.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zs.skinswitch.R
import com.zs.skinswitch.interfaces.SkinViewSupportInter
import com.zs.skinswitch.resource.ResourcesManager

/**
 * 自定义view以实现换肤功能
 */
class SkinFloatActionView : FloatingActionButton, SkinViewSupportInter {

    var bgTintColorId: Int = 0

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, 0)
        bgTintColorId =
            obtainStyledAttributes.getResourceId(R.styleable.FloatingActionButton_backgroundTint, 0)
    }

    override fun applySkin() {
        if (bgTintColorId != 0) {
            backgroundTintList = ResourcesManager.getColorStateList(bgTintColorId)
        }
    }
}