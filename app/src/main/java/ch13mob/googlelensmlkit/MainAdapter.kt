package ch13mob.googlelensmlkit

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.row_item_main.view.*

class MainAdapter(private val apiList: List<PojoApi>) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    lateinit var context: Context

    class MainHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val currItem = apiList[position]
        with(holder.itemView) {
            rih_api_name.text = currItem.title
            rih_api_desc.text = currItem.desc
            rih_api_img.setImageResource(currItem.imageId)
            rih_view_home.setOnClickListener {
                when (currItem.id) {
                    0 -> Toast.makeText(context, context.getString(R.string.am_work_in_progress), Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(context, context.getString(R.string.am_work_in_progress), Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(context, context.getString(R.string.am_work_in_progress), Toast.LENGTH_SHORT).show()
                    3 -> context.startActivity(Intent(context, ImageLabelActivity::class.java))
                    4 -> Toast.makeText(context, context.getString(R.string.am_work_in_progress), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        context = parent.context
        return MainHolder(LayoutInflater.from(context).inflate(R.layout.row_item_main, parent, false))
    }

    override fun getItemCount() = apiList.size
}