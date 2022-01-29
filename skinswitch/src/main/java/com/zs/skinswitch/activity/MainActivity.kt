package com.zs.skinswitch.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zs.skinswitch.R
import com.zs.skinswitch.fragment.HomeFragment
import com.zs.skinswitch.fragment.MineFragment
import com.zs.skinswitch.fragment.NewsFragment
import com.zs.skinswitch.lib.base.BaseActivity
import com.zs.skinswitch.lib.interfaces.SkinViewSupportInter
import com.zs.skinswitch.lib.resource.ResourcesManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener, SkinViewSupportInter {

    lateinit var homeFragment: HomeFragment
    lateinit var newsFragment: NewsFragment
    lateinit var mineFragment: MineFragment

    /**
     * 记录当前被选中的tab的位置
     * 0 主页  1 新闻  2 我的
     */
    var curTabSelectIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        CommonStyleUtil.setStatusTransparentStyle(window,false)

        initFragment()

        initListener()

        activity_main_home_rl.callOnClick()
    }

    private fun initListener() {
        activity_main_home_rl.setOnClickListener(this)
        activity_main_news_rl.setOnClickListener(this)
        activity_main_mine_rl.setOnClickListener(this)
    }

    private fun initFragment() {
        homeFragment = HomeFragment()
        newsFragment = NewsFragment()
        mineFragment = MineFragment()
    }

    private fun showFragment(fragment: Fragment) {
        val beginTransaction = this@MainActivity.supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.activity_main_fl, fragment)
        beginTransaction.commit()
    }

    override fun onClick(v: View?) {
        when (v) {
            activity_main_home_rl -> {
                curTabSelectIndex = 0
                updateTabUI()
                showFragment(homeFragment)
            }
            activity_main_news_rl -> {
                curTabSelectIndex = 1
                updateTabUI()
                showFragment(newsFragment)
            }
            activity_main_mine_rl -> {
                curTabSelectIndex = 2
                updateTabUI()
                showFragment(mineFragment)
            }
        }
    }

    private fun updateTabUI() {
        resetTabStatus()
        if (curTabSelectIndex == 0) {
            activity_main_home_iv.background = (ResourcesManager.getDrawable(R.drawable.home_pre))
            activity_main_home_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_press))
        } else if (curTabSelectIndex == 1) {
            activity_main_news_iv.background = (ResourcesManager.getDrawable(R.drawable.news_pre))
            activity_main_news_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_press))
        } else {
            activity_main_mine_iv.background = (ResourcesManager.getDrawable(R.drawable.mine_pre))
            activity_main_mine_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_press))
        }
    }

    private fun resetTabStatus() {
        activity_main_home_iv.background = (ResourcesManager.getDrawable(R.drawable.home))
        activity_main_home_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_normal))

        activity_main_news_iv.background = (ResourcesManager.getDrawable(R.drawable.news))
        activity_main_news_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_normal))

        activity_main_mine_iv.background = (ResourcesManager.getDrawable(R.drawable.mine))
        activity_main_mine_tv.setTextColor(ResourcesManager.getColor(R.color.home_tab_normal))
    }

    override fun applySkin() {
        updateTabUI()
    }
}