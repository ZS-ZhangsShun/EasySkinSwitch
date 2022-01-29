package com.zs.skinswitch.banner.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zs.skinswitch.R

class ImageTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var imageView : ImageView
    lateinit var title : TextView
    lateinit var time : TextView

    init {
        imageView = itemView.findViewById(R.id.image)
        title = itemView.findViewById(R.id.bannerTitle)
        time = itemView.findViewById(R.id.bannerTime)
    }
}