package org.vanmierlo.kapsamazing_app

import android.content.Intent.EXTRA_TEXT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val kapid = intent.getStringExtra(EXTRA_TEXT)
        println(kapid)
    }
}