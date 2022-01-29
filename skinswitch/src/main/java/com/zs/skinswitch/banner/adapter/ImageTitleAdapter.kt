package com.zs.skinswitch.banner.adapter

import com.youth.banner.adapter.BannerAdapter
import com.zs.skinswitch.banner.holder.ImageTitleHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.zs.skinswitch.R
import com.zs.skinswitch.lib.resource.ResourcesManager

/**
 * 自定义布局，图片+标题
 */
open class ImageTitleAdapter(mDatas: List<String>) : BannerAdapter<String, ImageTitleHolder>(mDatas) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ImageTitleHolder {
        return ImageTitleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.banner_image_title, parent, false)
        )
    }

    override fun onBindView(holder: ImageTitleHolder, data: String, position: Int, size: Int) {
        holder.imageView.setImageDrawable(ResourcesManager.getDrawable(R.drawable.other_banner1))
        val split = data.split("&")
        holder.title.text = split[0]
        holder.time.text = split[1]
    }
}