package com.yasin.dragapp

import android.content.ClipData
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.DragStartHelper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        DragStartHelper(findViewById(R.id.image_drag_item)) { view, _ ->
            val imageFile = File(File(filesDir, "images"), "samsung_image.png")
            if (!imageFile.exists()) {
                File(filesDir, "images").mkdirs()
                ByteArrayOutputStream().use { bos ->
                    (view as AppCompatImageView).drawable.toBitmap()
                        .compress(Bitmap.CompressFormat.PNG, 100, bos)
                    FileOutputStream(imageFile).use { fos ->
                        fos.write(bos.toByteArray())
                        fos.flush()
                    }
                }
            }

            val imageUri = FileProvider.getUriForFile(this, "com.yasin.dragapp.images", imageFile)
            val dragClipData = ClipData.newUri(contentResolver, "Image", imageUri)
            val dragShadow = View.DragShadowBuilder(view)
            view.startDragAndDrop(
                dragClipData,
                dragShadow,
                null,
                View.DRAG_FLAG_GLOBAL.or(View.DRAG_FLAG_GLOBAL_URI_READ)
            )
        }.attach()


    }
}