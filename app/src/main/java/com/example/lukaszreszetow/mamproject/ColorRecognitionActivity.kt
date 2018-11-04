package com.example.lukaszreszetow.mamproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_color_recognition.*
import org.opencv.android.CameraBridgeViewBase
import org.opencv.imgproc.Imgproc
import android.view.MotionEvent
import android.view.View
import android.util.Log
import android.widget.Toast
import org.opencv.core.*
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.BaseLoaderCallback



class ColorRecognitionActivity : AppCompatActivity(), View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {

    private val TAG = "CameraRecognitionActivity"

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    cameraView.enableView()
                    cameraView.setOnTouchListener(this@ColorRecognitionActivity)
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    private var mIsColorSelected = false
    private lateinit var mRgba: Mat
    private var mBlobColorRgba = Scalar(255.0)
    private var mBlobColorHsv = Scalar(255.0)
    private var mDetector = ColorDetector()
    private var CONTOUR_COLOR = Scalar(255.0, 0.0, 0.0, 255.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_color_recognition)
        cameraView.setCvCameraViewListener(this)
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        mRgba = inputFrame.rgba()

        if (mIsColorSelected) {
            mDetector.process(mRgba)
            val contours = mDetector.contours
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR)
        }

        return mRgba
    }

    private fun converScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val cols = mRgba.cols()
        val rows = mRgba.rows()

        val xOffset = (cameraView.width - cols) / 2
        val yOffset = (cameraView.height - rows) / 2

        val x = event.x.toInt() - xOffset
        val y = event.y.toInt() - yOffset


        if (x < 0 || y < 0 || x > cols || y > rows) return false

        val touchedRect = Rect()

        touchedRect.x = if (x > 4) x - 4 else 0
        touchedRect.y = if (y > 4) y - 4 else 0

        touchedRect.width = if (x + 4 < cols) x + 4 - touchedRect.x else cols - touchedRect.x
        touchedRect.height = if (y + 4 < rows) y + 4 - touchedRect.y else rows - touchedRect.y

        val touchedRegionRgba = mRgba.submat(touchedRect)

        val touchedRegionHsv = Mat()
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL)

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv)
        val pointCount = touchedRect.width * touchedRect.height
        for (i in 0 until mBlobColorHsv.`val`.size)
            mBlobColorHsv.`val`[i] = (mBlobColorHsv.`val`[i] / pointCount)

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv)

        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.`val`[0] + ", " + mBlobColorRgba.`val`[1] +
                ", " + mBlobColorRgba.`val`[2] + ", " + mBlobColorRgba.`val`[3] + ")")

        if(mBlobColorRgba.`val`[0] >=200 && mBlobColorRgba.`val`[1] >=200 && mBlobColorRgba.`val`[2] >=200){
            Toast.makeText(this, "Wykryto bialy kolor, ekran zamkniety !", Toast.LENGTH_LONG).show()
            finish()
        }

        mDetector.setHsvColor(mBlobColorHsv)

        mIsColorSelected = true

        touchedRegionRgba.release()
        touchedRegionHsv.release()

        return false // don't need subsequent touch events
    }

    public override fun onPause() {
        super.onPause()
        if (cameraView != null)
            cameraView.disableView()
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
}
