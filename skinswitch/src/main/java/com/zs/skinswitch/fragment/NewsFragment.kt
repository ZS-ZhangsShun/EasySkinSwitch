package com.zs.skinswitch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zs.easy.common.constants.EasyVariable
import com.zs.skinswitch.R
import com.zs.skinswitch.lib.manager.SkinManager
import kotlinx.android.synthetic.main.fragment_news.*
import java.io.File

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //换肤
        fra_home_skin_new_btn.setOnClickListener {
            SkinManager.loadSkin(EasyVariable.mContext.cacheDir.absolutePath
                    + File.separator + "skinonly-debug.apk")
        }
        //恢复默认皮肤
        fra_home_skin_default_btn.setOnClickListener {
            SkinManager.loadSkin(null)
        }
    }
}