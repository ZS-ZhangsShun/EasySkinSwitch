package com.zs.skinswitch.lib.resource

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.graphics.drawable.Drawable
import android.content.res.ColorStateList
import com.zs.easy.common.utils.LogUtil

/**
 * 资源管理器
 */
object ResourcesManager {
    var mSkinPkgName: String? = null
    var isDefaultSkin = true

    // app原始的resource
    var mAppResources: Resources? = null

    // 皮肤包的resource
    var mSkinResources: Resources? = null

    fun init(context: Context) {
        mAppResources = context.resources
    }

    fun applySkin(resources: Resources?, pkgName: String ?) {
        mSkinResources = resources
        mSkinPkgName = pkgName
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }

    fun reset() {
        mSkinResources = null
        mSkinPkgName = ""
        isDefaultSkin = true
    }

    /**
     * 在加载资源时我们可以通过宿主app的资源id 得到资源名称和类型，然后在通过名称和类型找到皮肤包中对应资源id
     */
    private fun getIdFromSkinResource(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        val resName = mAppResources!!.getResourceEntryName(resId)
        val resType = mAppResources!!.getResourceTypeName(resId)
        LogUtil.i("resId = $resId resName = $resName resType= $resType")
        return mSkinResources!!.getIdentifier(resName, resType, mSkinPkgName)
    }

    /**
     * 输入主APP的ID，到皮肤APK文件中去找到对应ID的颜色值
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        if (isDefaultSkin) {
            return mAppResources!!.getColor(resId)
        }
        val skinId: Int = getIdFromSkinResource(resId)
        return if (skinId == 0) {
            mAppResources!!.getColor(resId)
        } else mSkinResources!!.getColor(skinId)
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        if (isDefaultSkin) {
            return mAppResources!!.getColorStateList(resId)
        }
        val skinId: Int = getIdFromSkinResource(resId)
        return if (skinId == 0) {
            mAppResources!!.getColorStateList(resId)
        } else mSkinResources!!.getColorStateList(skinId)
    }

    fun getDrawable(resId: Int): Drawable {
        if (isDefaultSkin) {
            return mAppResources!!.getDrawable(resId)
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        val skinId: Int = getIdFromSkinResource(resId)
        return if (skinId == 0) {
            mAppResources!!.getDrawable(resId)
        } else mSkinResources!!.getDrawable(skinId)
    }


    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    fun getBackground(resId: Int): Any {
        val resourceTypeName = mAppResources!!.getResourceTypeName(resId)
        return if ("color" == resourceTypeName) {
            getColor(resId)
        } else {
            // drawable
            getDrawable(resId)
        }
    }
}