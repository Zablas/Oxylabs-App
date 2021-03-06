package com.example.oxylabs_app

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(
    private val context: Context,
    private val notifications: ArrayList<NotificationDTO>,
    private val database: NotificationDatabaseHelper)
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
        holder.rowLayout.setOnClickListener { checkNotificationForValidity(position) }
        val animation = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
        holder.rowLayout.animation = animation
    }

    override fun getItemCount(): Int = notifications.size

    private fun checkNotificationForValidity(position: Int) {
        val cursor = database.getNotification(notifications[position].id.toString())
        if (isNotificationInDatabase(cursor)) openEditForm(position)
        else Toast.makeText(context, R.string.refresh_request_toast, Toast.LENGTH_SHORT).show()
    }

    private fun isNotificationInDatabase(cursor: Cursor): Boolean = cursor.moveToNext()

    private fun openEditForm(position: Int) {
        val intent = Intent(context, EditNotificationActivity::class.java)
        val notification = NotificationDTO(
            notifications[position].id,
            notifications[position].title,
            notifications[position].description,
            notifications[position].scheduledTime
        )
        intent.putExtra(NotificationDTO.ACTIVITY_EXTRA_NAME, notification)
        context.startActivity(intent)
    }
}