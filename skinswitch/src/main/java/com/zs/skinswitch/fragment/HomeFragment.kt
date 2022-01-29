package com.zs.skinswitch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.zs.skinswitch.banner.adapter.ImageTitleAdapter
import com.zs.skinswitch.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBannerWithFixedData()

        val titleData = arrayListOf(
            "各地疫情防控措施&2021.12.14",
            "防范电信网络诈骗手册&2021.12.14",
            "2022年全国发大财&2021.12.14",
            "2022年美国经济危机爆发&2021.12.14"
        )
        fra_home_banner2
            .addBannerLifecycleObserver(activity)
            .setAdapter(ImageTitleAdapter(titleData))
            .setIndicator(indicator, false)
            .scrollTime = 100

        fra_home_body_jinjing_tv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }
        })
    }

    private fun initBannerWithFixedData() {
        val topBannerImgIDs = arrayListOf(
            R.drawable.home_top_banner1,
            R.drawable.home_top_banner2
        )
        fra_home_banner.addBannerLifecycleObserver(activity)
            .setAdapter(object : BannerImageAdapter<Int>(topBannerImgIDs) {
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: Int,
                    position: Int,
                    size: Int
                ) {
                    holder.imageView.scaleType = ImageView.ScaleType.FIT_XY
                    holder.imageView.setImageResource(data)
                }

            })
            .setIndicator(CircleIndicator(activity)).scrollTime = 100
    }

}