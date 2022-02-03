package com.zs.skinswitch.util

import android.content.Context
import android.widget.Toast
import com.zs.easy.common.utils.LogUtil

/**
 * 吐丝工具
 */
fun Context.toast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}

fun <T> T.log(content: String) {
    LogUtil.i("日志打印 ${this.toString()}")
}
