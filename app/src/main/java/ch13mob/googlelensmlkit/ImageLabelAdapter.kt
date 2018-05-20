package ch13mob.googlelensmlkit

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import kotlinx.android.synthetic.main.row_item.view.*

class ImageLabelAdapter(private val firebaseVisionList: List<Any>, private val isCloud: Boolean)
    : RecyclerView.Adapter<ImageLabelAdapter.ItemHolder>() {

    lateinit var context: Context

    inner class ItemHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindCloud(currentItem: FirebaseVisionCloudLabel) {
            when {
                currentItem.confidence > .70 -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.green))
                currentItem.confidence < .30 -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.red))
                else -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
            itemView.ri_name.text = currentItem.label
            itemView.ri_accuracy.text = context.getString(R.string.ail_accuracy) + (currentItem.confidence * 100).toInt() + "%"
        }

        @SuppressLint("SetTextI18n")
        fun bindDevice(currentItem: FirebaseVisionLabel) {
            when {
                currentItem.confidence > .70 -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.green))
                currentItem.confidence < .30 -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.red))
                else -> itemView.ri_accuracy.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
            itemView.ri_name.text = currentItem.label
            itemView.ri_accuracy.text = context.getString(R.string.ail_accuracy) + (currentItem.confidence * 100).toInt() + "%"
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = firebaseVisionList[position]
        if (isCloud)
            holder.bindCloud(currentItem as FirebaseVisionCloudLabel)
        else
            holder.bindDevice(currentItem as FirebaseVisionLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        context = parent.context
        return ItemHolder(LayoutInflater.from(context).inflate(R.layout.row_item, parent, false))
    }

    override fun getItemCount() = firebaseVisionList.size
}