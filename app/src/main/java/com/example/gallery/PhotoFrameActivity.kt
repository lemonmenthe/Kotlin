package com.example.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer
import java.util.*

class PhotoFrameActivity: AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById(R.id.backgroundPhotoImageView)
    }

    private val photoImageView: ImageView by lazy {
        findViewById(R.id.photoImageView)
    }

    private var currentPosition = 0

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        Log.d("photoFrame","onCreate")
        getPhotoUriFromList()
    }

    //intent로 값을 가져오는부분
    private fun getPhotoUriFromList(){
        val size = intent.getIntExtra("photoListSize",0)
        for (i in 0..size){
            intent.getStringExtra("photo$i")?.let{
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer(){
        timer = kotlin.concurrent.timer(period = 5 * 1000) {
            runOnUiThread(){

                Log.d("PhotoFrame","5초 지나감")

                val current = currentPosition
                val next = if(photoList.size <= currentPosition + 1) 0 else
                    currentPosition + 1

                    backgroundPhotoImageView.setImageURI(photoList[current])

                    photoImageView.alpha = 0f  //투명도
                    photoImageView.setImageURI(photoList[next])
                    photoImageView.animate()//효과 불투명 , 1초 주기로 슬라이드
                        .alpha(1.0f)
                        .setDuration(1000)
                        .start()

                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()

        Log.d("PhotoFrame", "onStop! Timer Cancle")
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        Log.d("PhotoFrame", "onStart! Timer Start")
        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PhotoFrame", "onDestroy! Timer Cancle")
        timer?.cancel()
    }
}