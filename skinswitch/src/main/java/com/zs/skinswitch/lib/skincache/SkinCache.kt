package com.zs.skinswitch.lib.skincache

import android.util.AttributeSet
import android.view.View
import com.zs.skinswitch.lib.interfaces.SkinViewSupportInter
import com.zs.skinswitch.lib.theme.SkinThemeUtils

/**
 * 用于缓存需要换肤的view
 */
class SkinCache {
    private val mAttributes: MutableList<String> = mutableListOf(
        "background",
        "src",
        "textColor",
        "drawableLeft",
        "drawableTop",
        "drawableRight",
        "drawableBottom"
    )

    //记录换肤需要操作的View与属性信息
    private val mSkinViews: MutableList<SkinView> = mutableListOf()

    /**
     * 检测并缓存view
     * 如果不支持换肤 则不缓存
     */
    fun checkAndCache(view: View, attrs: AttributeSet): SkinView? {
        val mSkinPars: MutableList<SkinPair> = ArrayList()

        for (i in 0 until attrs.attributeCount) {
            //获得属性名  textColor/background
            val attributeName: String = attrs.getAttributeName(i)
            if (mAttributes.contains(attributeName)) {
                // #1545634635
                // ?722727272
                // @722727272
                val attributeValue: String = attrs.getAttributeValue(i)
                // 比如color 以#开头表示写死的颜色 不可用于换肤
                if (attributeValue.startsWith("#")) {
                    continue
                }
                // 以 ？开头的表示使用 属性
                val resId: Int = if (attributeValue.startsWith("?")) {
                    val attrId = attributeValue.substring(1).toInt()
                    SkinThemeUtils.getResId(view.context, intArrayOf(attrId)).get(0)
                } else {
                    // 正常以 @ 开头
                    attributeValue.substring(1).toInt()
                }
                val skinPair = SkinPair(attributeName, resId)
                mSkinPars.add(skinPair)
            }
        }

        if (mSkinPars.isNotEmpty() || view is SkinViewSupportInter) {
            val skinView = SkinView(view, mSkinPars)
            mSkinViews.add(skinView)
            return skinView
        } else {
            return null
        }
    }

    /**
     * 对所有的view中的所有的属性进行皮肤修改
     */
    fun applySkin() {
        for (mSkinView in mSkinViews) {
            mSkinView.applySkin()
        }
    }

    fun clear() {
        mAttributes.clear()
        mSkinViews.clear()
    }
}