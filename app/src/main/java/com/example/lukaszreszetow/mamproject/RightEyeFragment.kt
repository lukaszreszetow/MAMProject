package com.example.lukaszreszetow.mamproject

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_right_eye.*


class RightEyeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_right_eye, container, false)
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        rightEye.setLifecycleOwner(viewLifecycleOwner)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        rightEye.start()
//    }
//
//    override fun onPause() {
//        rightEye.stop()
//        super.onPause()
//    }
//
//    override fun onDestroy() {
//        rightEye.destroy()
//        super.onDestroy()
//    }
}
