package com.yasin.dropapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.draganddrop.DropHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        DropHelper.configureView(this,
            findViewById(R.id.image_drop_target),
            arrayOf("image/*"),
            DropHelper.Options.Builder()
                .setHighlightColor(getColor(R.color.purple_500))
                .setHighlightCornerRadiusPx(6).build()
        ) { view, payload ->
            resetDropTarget()
            findViewById<AppCompatImageView>(R.id.image_drop_target).setImageURI(payload.clip.getItemAt(0).uri)
            payload
        }

        findViewById<AppCompatImageButton>(R.id.button_clear).setOnClickListener {
            resetDropTarget()
        }

    }

    private fun resetDropTarget() {
        findViewById<AppCompatImageView>(R.id.image_drop_target).setImageDrawable(null)
    }

}