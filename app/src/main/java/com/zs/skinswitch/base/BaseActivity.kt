package com.zs.skinswitch.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zs.skinswitch.R
import com.zs.skinswitch.theme.SkinThemeUtils

/**
 * 基类Activity 统一设置主题啥的
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinThemeUtils.updateStatusBarColor(this, R.color.status_bar)
    }
}