package com.hardik.mahant.coroutine_file_download

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE = 1024

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlList = arrayOf(
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
        )
        val adapter = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_list_item_1, urlList
        )
        listView.adapter = adapter

        if(!hasAllPermissions()){
            requestPermission()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            if (hasAllPermissions()) {
                startActivity(
                    Intent(this@MainActivity, VideoActivity::class.java)
                        .putExtra("videoURL", urlList[position])
                )
            } else {
                requestPermission()
            }
        }
    }

    private fun hasAllPermissions(): Boolean {
        return (
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this@MainActivity, "Click on URL to load the video", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
