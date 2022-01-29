package com.zs.skinswitch.application

import android.app.Application
import com.zs.easy.common.EasyCommon
import com.zs.easy.common.constants.EasyConstants
import com.zs.easy.common.utils.LogUtil
import com.zs.skinswitch.constants.ZSConstants
import com.zs.skinswitch.lib.manager.SkinManager

class ZSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EasyCommon.init(this, "ZSSkin_SP", true)
        EasyConstants.TAG = ZSConstants.TAG
        EasyConstants.isShowToast = true
        LogUtil.setDefaultFileName(ZSConstants.DEFAULT_LOG_FILE_NAME)

        SkinManager.init(this)
    }
}