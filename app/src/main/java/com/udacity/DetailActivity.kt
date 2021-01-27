package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Region
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var status: String? = ""
    private var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras!!
        downloadID = extras.getLong("downloadID")
        status = extras.getString("status")
        url = extras.getString("url")
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        Timber.i("$downloadID")
        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()
        val viewID: TextView = findViewById(R.id.download_id)
        viewID.text = downloadID.toString()
        val viewStatus: TextView = findViewById(R.id.download_status)
        viewStatus.text = status
        val viewUrl: TextView = findViewById(R.id.download_url)
        viewUrl.text = url

        ok_button.setOnClickListener {
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
