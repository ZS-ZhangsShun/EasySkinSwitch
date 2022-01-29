package com.zs.skinswitch.lib.manager

import android.app.Application
import com.zs.skinswitch.lib.actlifecallback.SkinActLifeCallback
import com.zs.skinswitch.lib.resource.ResourcesManager
import java.util.*
import android.content.pm.PackageManager

import android.content.res.AssetManager
import android.content.res.Resources
import android.text.TextUtils
import com.zs.easy.common.constants.EasyVariable
import com.zs.skinswitch.lib.constants.SkinConstants
import com.zs.skinswitch.lib.theme.SkinThemeUtils
import java.lang.Exception
import java.lang.reflect.Method


/**
 * 继承自Observable 的目的是使用系统写好的这套观察者模式
 * 后面可以直接调用notifyObservers方法来通知各个Activity的观察者（SkinLayoutInflaterFactory）去更新皮肤
 */
object SkinManager : Observable() {
    /**
     * 初始化换肤相关代码
     */
    fun init(app: Application) {
        //注册Activity生命周期监听
        app.registerActivityLifecycleCallbacks(SkinActLifeCallback())
        //初始化资源
        ResourcesManager.init(app)
        //如果用户有设置皮肤 则加载
        val skinPath = EasyVariable.spCommon.getString(SkinConstants.SP_SKIN_PATH, "")
        if (!TextUtils.isEmpty(skinPath)) {
            loadSkin(skinPath)
        }
    }

    /**
     * 记载皮肤并应用
     *
     * @param skinPath 皮肤路径 如果为空则使用默认皮肤
     */
    fun loadSkin(skinPath: String?) {
        if (TextUtils.isEmpty(skinPath)) {
            //还原默认皮肤
            ResourcesManager.reset()
            //保存
            EasyVariable.spCommon.edit().putString(SkinConstants.SP_SKIN_PATH, "").apply()
        } else {
            try {
                //宿主app的 resources;
                val appResource: Resources = EasyVariable.mContext.resources
                //反射创建AssetManager 与 Resource
                val assetManager = AssetManager::class.java.newInstance()
                //资源路径设置 目录或压缩包
                val addAssetPath: Method = assetManager.javaClass.getMethod(
                    "addAssetPath",
                    String::class.java
                )
                addAssetPath.invoke(assetManager, skinPath)

                //根据当前的设备显示器信息 与 配置(横竖屏、语言等) 创建Resources
                val skinResource =
                    Resources(assetManager, appResource.displayMetrics, appResource.configuration)

                //获取外部Apk(皮肤包) 包名
                val mPm = EasyVariable.mContext.packageManager
                val info = mPm.getPackageArchiveInfo(skinPath!!, PackageManager.GET_ACTIVITIES)
                val packageName = info?.packageName
                ResourcesManager.applySkin(skinResource, packageName)

                //保存
                EasyVariable.spCommon.edit().putString(SkinConstants.SP_SKIN_PATH, skinPath).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //通知采集的View 更新皮肤
        //被观察者改变 通知所有观察者
        setChanged()
        notifyObservers(null)
    }
}