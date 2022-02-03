package com.zs.skinswitch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zs.skinswitch.R
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment() {

    var exit: String? = null;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fra_mine_tv1.setOnClickListener {
            exit()
        }
        fra_mine_tv2.setOnClickListener {
            exit()
        }
    }

    private fun exit() {
        exit!!.length
    }
}