package ch13mob.googlelensmlkit

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_image_label.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*

class ImageLabelActivity : AppCompatActivity() {

    private var itemsList: ArrayList<Any> = ArrayList()
    private lateinit var itemAdapter: ImageLabelAdapter

    private lateinit var sheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_label)

        lbs_labels_list.layoutManager = LinearLayoutManager(this)
        ail_retry_btn.setOnClickListener {
            if (ail_camera_view.visibility == View.VISIBLE) showPreview() else hidePreview()
        }

        sheetBehavior = BottomSheetBehavior.from(ail_bottom_layout)
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                ail_fab_take_photo.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()
            }
        })

        ail_fab_take_photo.setOnClickListener {
            ail_fab_progress_circle.show()
            ail_camera_view.captureImage { cameraKitImage ->
                getLabelFromDevice(cameraKitImage.bitmap)
                runOnUiThread {
                    showPreview()
                    ail_image_preview.setImageBitmap(cameraKitImage.bitmap)
                }
            }
        }
    }

    private fun getLabelFromCloud(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().visionCloudLabelDetector

        itemsList.clear()

        detector.detectInImage(image)
                .addOnSuccessListener {
                    ail_fab_progress_circle.hide()
                    itemsList.addAll(it)
                    itemAdapter = ImageLabelAdapter(itemsList, true)
                    am_item_list.adapter = itemAdapter
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
                .addOnFailureListener {
                    ail_fab_progress_circle.hide()
                    Toast.makeText(baseContext, getString(R.string.ail_sorry), Toast.LENGTH_SHORT).show()
                }
    }

    private fun getLabelFromDevice(bitmap: Bitmap) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().visionLabelDetector

        itemsList.clear()

        detector.detectInImage(image)
                .addOnSuccessListener {
                    ail_fab_progress_circle.hide()
                    itemsList.addAll(it)
                    itemAdapter = ImageLabelAdapter(itemsList, false)
                    lbs_labels_list.adapter = itemAdapter
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                }
                .addOnFailureListener {
                    ail_fab_progress_circle.hide()
                    Toast.makeText(baseContext, getString(R.string.ail_sorry), Toast.LENGTH_SHORT).show()
                }
    }

    override fun onResume() {
        super.onResume()
        ail_camera_view.start()
    }

    override fun onPause() {
        super.onPause()
        ail_camera_view.stop()
    }

    private fun showPreview() {
        ail_frame_preview.visibility = View.VISIBLE
        ail_camera_view.visibility = View.GONE
    }

    private fun hidePreview() {
        ail_frame_preview.visibility = View.GONE
        ail_camera_view.visibility = View.VISIBLE
    }
}
