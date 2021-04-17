package com.example.oxylabs_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val context: Context, private val notifications: ArrayList<NotificationDTO>)
    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewScheduledTime: TextView = itemView.findViewById(R.id.textViewScheduledTime)
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
    }

    override fun getItemCount(): Int = notifications.size
}