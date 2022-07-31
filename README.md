# Drag and Drop Across Android Apps

In this article, we will learn about the Drag and Drop feature between two apps which is applicable for split-screen mode.

## Intro:
Drag and Drop is a feature that increases user accessibility with a better user experience. It's a handy gesture where users move or copy the content of a view that is represented by a URI in the same app or another app. Our main goal is to move or copy image between apps in a split-screen mood. So, there will be two apps one contains the feature of drag and another one contains a drop view.
Android started drag and drop gesture support since API level 11 using DragEvent class. But recently they released a new Jetpack DragAndDrop library to make the implementation easier with less code.
As it helps the user to work faster while also giving a realistic user experience, I would say it is the most demanding feature for all relevant apps.

## Drag App:
The drag app has only one activity, which contains the implementation of the view drag feature. Below we will see a step-by-step Drag app implementation.

## Step 1:
We mentioned that we will be working with images, so we need to use a provider that is required to save images to our local storage. We have added the required provider to the AndroidManifest.xml as shown in the code below.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.yasin.dragapp">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.DragApp"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.yasin.dragapp.images"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/filepaths" />
        </provider>
    </application>
</manifest>
```

## Step 2:
We have taken a TextView and an ImageView in the activity_main.xml layout. The TextView contains the text for guiding the user and the ImageView is our main content which we drag and drop into another application. Code for the activity_main.xml layout is below.

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/text_drag_item"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:padding="20dp"
        android:text="Long press and Drag this image and drop to Drop App"
        android:textAlignment="center"
        android:textAppearance="@style/TextAppearance.AppCompat.Large"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.appcompat.widget.AppCompatImageView
        android:id="@+id/image_drag_item"
        android:layout_width="422dp"
        android:layout_height="0dp"
        android:layout_marginTop="16dp"
        android:adjustViewBounds="true"
        android:scaleType="fitCenter"
        android:src="@drawable/samsung_image"
        app:layout_constraintBottom_toTopOf="@+id/guideline_center"
        app:layout_constraintEnd_toEndOf="@+id/text_drag_item"
        app:layout_constraintHorizontal_bias="0.495"
        app:layout_constraintStart_toStartOf="@+id/text_drag_item"
        app:layout_constraintTop_toBottomOf="@+id/text_drag_item" />



    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline_center"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.9" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 3:
Android has created a class called "DragStartHelper" for drag operations, this class takes a View and an OnDragStartListener as parameters. This class enables the drag feature on that view which it takes as a parameter. The newly created helper is not initially attached to the view, attach method must be called explicitly. The other parameter is an interface that is invoked when a drag start gesture is detected.
First, we need to create the image file that we are working with and check the file location to see if it exists. If not, we will save the image from the ImageView, we will get a URI from the image file which will be needed in the next implementation. To start the drag operation we need to call the view.startDragAndDrop() method which will draw the view immediately after a long press and drag under the touched coordinate of the screen. startDragAndDrop() method takes four parameters chronologically, ClipData which are obtained from the URI of the dragged view, View.DragShadowBuilder which draws the shadow in the touched coordinates, localState sends small size information from the dragged View to the target View, and Flag controls the drag and drop operation. Example codes are below,

```
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
```


