package com.example.oxylabs_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val context: Context, private val notifications: ArrayList<NotificationDTO>)
    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewScheduledTime: TextView = itemView.findViewById(R.id.textViewScheduledTime)
        val rowLayout: LinearLayout = itemView.findViewById(R.id.rowLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.notification_data_row, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.textViewTitle.text = notifications[position].title
        holder.textViewDescription.text = notifications[position].description
        holder.textViewScheduledTime.text = notifications[position].scheduledTime
        holder.rowLayout.setOnClickListener { navigateToEditActivity(position) }
    }

    override fun getItemCount(): Int = notifications.size

    fun navigateToEditActivity(position: Int) {
        val intent = Intent(context, EditNotificationActivity::class.java)
        val notification = NotificationDTO(
            notifications[position].id,
            notifications[position].title,
            notifications[position].description,
            notifications[position].scheduledTime
        )
        intent.putExtra("notification", notification)
        context.startActivity(intent)
    }
}