package com.zs.skinswitch.constants

import android.app.Application
import android.content.SharedPreferences

object SkinConstants {
    const val SP_TAG: String = "SP_EasySkinSwitch"
    const val SP_SKIN_PATH: String = "SP_SKIN_PATH"
    const val TAG: String = "EasySkinSwitch"
    var appContext: Application? = null
    var spCommon: SharedPreferences? = null
}