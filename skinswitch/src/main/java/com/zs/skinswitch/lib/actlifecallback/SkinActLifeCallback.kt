package com.zs.skinswitch.lib.actlifecallback

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import androidx.core.view.LayoutInflaterCompat
import com.zs.easy.common.utils.LogUtil
import com.zs.skinswitch.lib.factory.SkinLayoutInflaterFactory
import com.zs.skinswitch.lib.manager.SkinManager

class SkinActLifeCallback : Application.ActivityLifecycleCallbacks {
    private val mLayoutInflaterFactories: ArrayMap<Activity, SkinLayoutInflaterFactory> = ArrayMap()
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //在此处对Activity进行view生产工厂的设置，将会在setContentView之前执行此处代码
        //原理是：Activity的源码中在onCreate方法中会执行dispatchActivityCreated方法
        //然后就会回调到这里来，这一步在我们写的Activity的onCreate方法中的super.onCreate(savedInstanceState)里来执行的
        //而setContentView是在super.onCreate(savedInstanceState)之后调用
        //因为系统只允许设置一次factory 所以这里通过反射修改对应的系统变量 实现多次设置 但是 9.0以上不允许在反射修改mFactorySet 的值
        //因此我们这里直接通过反射给进行赋值 下面这个方法就是源码设置Factory2的地方 我们反射实现mFactorySet = true后面部分的代码
//        fun setFactory2(factory: Factory2?) {
//            check(!mFactorySet) { "A factory has already been set on this LayoutInflater" }
//            if (factory == null) {
//                throw NullPointerException("Given factory can not be null")
//            }
//            mFactorySet = true
//            if (mFactory == null) {
//                mFactory2 = factory
//                mFactory = mFactory2
//            } else {
//                mFactory2 = LayoutInflater.FactoryMerger(factory, factory, mFactory, mFactory2)
//                mFactory = mFactory2
//            }
//        }
        val factory2 = SkinLayoutInflaterFactory(activity)
        LogUtil.i("cur sdk version is ${Build.VERSION.SDK_INT}")
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            //因为系统只允许设置一次factory 所以这里通过反射修改对应的系统变量 实现多次设置
            try {
                val javaClass = LayoutInflater::class.java
                val declaredField = javaClass.getDeclaredField("mFactorySet")
                declaredField.isAccessible = true
                declaredField.set(activity.layoutInflater, false)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            //此种方式不兼容5.0以下
//        activity.layoutInflater.factory2 = null
            //这种设置方式更好，系统对5.0以下做了兼容处理
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, factory2)
        } else {
            val layoutInflater = activity.layoutInflater
            val javaClass = LayoutInflater::class.java
            try {
                val mFactory2 = javaClass.getDeclaredField("mFactory2")
                val mFactory = javaClass.getDeclaredField("mFactory")
                mFactory2.isAccessible = true
                mFactory.isAccessible = true
                val mFactoryValue = mFactory.get(layoutInflater)
                val mFactory2Value = mFactory2.get(layoutInflater)
                if (mFactoryValue == null) {
                    mFactory2.set(layoutInflater, factory2)
                    mFactory.set(layoutInflater, factory2)
                } else {
                    val clazz = Class.forName("android.view.LayoutInflater\$FactoryMerger")
                    val size2 = clazz.declaredConstructors.size
                    LogUtil.i("size2=$size2")

                    val constructor = clazz.getConstructor(
                        LayoutInflater.Factory::class.java,
                        Factory2::class.java,
                        LayoutInflater.Factory::class.java,
                        Factory2::class.java
                    )
                    constructor.isAccessible = true
                    val newInstance = constructor.newInstance(
                        factory2,
                        factory2,
                        mFactoryValue as LayoutInflater.Factory,
                        mFactory2Value as Factory2
                    )
                    mFactory2.set(layoutInflater, newInstance)
                    mFactory.set(layoutInflater, newInstance)
                }
            } catch (e: Exception) {
                try {
                    val mFactory2 = javaClass.getDeclaredField("mFactory2")
                    val mFactory = javaClass.getDeclaredField("mFactory")
                    mFactory2.isAccessible = true
                    mFactory.isAccessible = true
                    mFactory2.set(layoutInflater, factory2)
                    mFactory.set(layoutInflater, factory2)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        //设置完工厂 还要将工厂作为观察者缓存起来，动态调用换肤功能时，可以通知所有工厂进行换肤工作
        //这里利用系统自带的观察者类Observable来实现 SkinManager 继承自Observable
        //SkinLayoutInflaterFactory 实现 Observal接口
        mLayoutInflaterFactories[activity] = factory2
        SkinManager.addObserver(factory2)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        //在这里把作为观察者的工厂移除
        val factory2: SkinLayoutInflaterFactory? = mLayoutInflaterFactories.remove(activity)
        //释放资源
        factory2?.destroy()
        SkinManager.deleteObserver(factory2)
    }
}