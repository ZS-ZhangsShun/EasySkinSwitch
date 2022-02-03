package com.zs.skinswitch.factory

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.zs.skinswitch.R
import com.zs.skinswitch.constants.SkinConstants
import com.zs.skinswitch.interfaces.SkinViewSupportInter
import com.zs.skinswitch.resource.ResourcesManager
import com.zs.skinswitch.skincache.SkinCache
import com.zs.skinswitch.theme.SkinThemeUtils
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.*

/**
 * view 生产工厂类，用于代替系统进行view生产 生产过程参考系统源码即可
 * 1、在生成过程中对需要换肤的view进行缓存
 * 2、如果已经设置了相关皮肤，生成完view后立刻对其进行相应皮肤的设置
 */
class SkinLayoutInflaterFactory : LayoutInflater.Factory2, Observer {

    var activity: Activity? = null
    var skinCache: SkinCache? = null

    //记录View的构造函数结构，通过两个参数的构造函数去反射view 这里参考系统源码
    private val mConstructorSignature = arrayOf(
        Context::class.java, AttributeSet::class.java
    )

    //缓存view的构造函数 参考系统源码
    private val sConstructorMap = HashMap<String, Constructor<out View>>()

    private val mClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )

    constructor(activity: Activity) {
        this.activity = activity
        skinCache = SkinCache()
    }


    /**
     * 对于Factory2 系统会回调4个参数的方法 在源码中可以看到
     * 详见 LayoutInflater --- tryCreateView 方法
     */
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //先尝试按照系统的view（"android.widget.xxx", "android.webkit.xxx", "android.app.xxx", "android.view.xxx"）去创建
        var view: View? = tryCreateView(name, context, attrs)
        if (null == view) {
            //如果不是这几个包下的view 直接通过反射创建
            view = createView(name, context, attrs)
        }
        if (null != view) {
//            Log.i(SkinConstants.TAG, "success create view $name ")
            //1.如果是可以换肤的view则缓存相关信息
            val skinView = skinCache?.checkAndCache(view, attrs)
            //2.判断是否有设置的皮肤，有则对支持换肤的view进行换肤
            if (!ResourcesManager.isDefaultSkin) {
                skinView?.applySkin()
            }
        } else {
            Log.i(SkinConstants.TAG, "view is null ${attrs.getAttributeName(0)}")
        }
        return view
    }

    private fun tryCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        //如果包含 . 可能是自定义view,或者谷歌出的一些支持库或者Material Design里面的view等
        //总之 就是 xxx.xxx.xxx这种格式的view
        if (-1 != name.indexOf('.')) {
            return null
        }
        //不包含就要在解析的 节点 name前，拼上： android.widget. 等尝试去反射
        for (i in mClassPrefixList.indices) {
            val view = createView(mClassPrefixList[i].toString() + name, context, attrs)
            if (view != null) {
                return view
            }
        }
        return null
    }

    /**
     * 通过反射创建view 参考系统源码
     */
    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor: Constructor<out View>? = findConstructor(context, name)
        try {
            return constructor?.newInstance(context, attrs)
        } catch (e: Exception) {
        }
        return null
    }

    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor: Constructor<out View>? = sConstructorMap[name]
        if (constructor == null) {
            try {
                val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
                constructor =
                    clazz.getConstructor(mConstructorSignature[0], mConstructorSignature[1])
                sConstructorMap[name] = constructor
            } catch (e: Exception) {
            }
        }
        return constructor
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    /**
     * 观察者模式 有通知来就执行 用户手动点击换肤 会通过
     * SkinManager.notifyObservers通知过来
     */
    override fun update(o: Observable?, arg: Any?) {
        SkinThemeUtils.updateStatusBarColor(activity!!, R.color.status_bar)
        skinCache?.applySkin()
        //MainActivity 里面动态设置底部tab样式需要单独处理
        if (activity is SkinViewSupportInter) {
            (activity as SkinViewSupportInter).applySkin()
        }
    }

    /**
     * 释放当前缓存的资源
     */
    fun destroy() {
        sConstructorMap.clear()
        skinCache?.clear()
    }
}