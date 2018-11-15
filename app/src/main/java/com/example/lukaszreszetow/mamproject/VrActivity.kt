package com.example.lukaszreszetow.mamproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_vr.*
import kotlinx.android.synthetic.main.fragment_right_eye.*
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter
import com.github.pwittchen.reactivesensors.library.ReactiveSensors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_color_recognition.*
import kotlinx.android.synthetic.main.activity_sensors.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.CvType.CV_8UC3
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.IOException
import java.util.Observable


class VrActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    var accX = 0f
    var accY = 0f
    var accZ = 0f

    private lateinit var mRgba: Mat
    private val TAG = "VrActivity"

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    leftEye.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_vr)
        leftEye.setCvCameraViewListener(this)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onPause() {
        super.onPause()
        if (leftEye != null)
            leftEye.disableView()
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        mRgba = inputFrame.rgba()
        val conf = Bitmap.Config.ARGB_8888 // see other conf types

        val bmp = Bitmap.createBitmap(
            mRgba.size().width.toInt(),
            mRgba.size().height.toInt(),
            conf
        )
        Utils.matToBitmap(mRgba, bmp)
        val resizedBitmapRight = Bitmap.createBitmap(bmp, 82, 0, 782, 480)
        val resizedBitmapLeft = Bitmap.createBitmap(bmp, 0, 0, 782, 480)
        val drawableRight = BitmapDrawable(resources, resizedBitmapRight)
        val drawableLeft = BitmapDrawable(resources, resizedBitmapLeft)
        runOnUiThread {
            rightEye.setImageDrawable(drawableRight)
            leftEyeMirror.setImageDrawable(drawableLeft)
        }
        val blackMat = Mat(Size(864.0, 480.0), CV_8UC3, Scalar(0.0, 0.0, 0.0))
        return blackMat
    }

}
