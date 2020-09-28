package com.example.hw3

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactRecyclerViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    fun bind(contact: Contact) {
        with(root) {
            contact_name.text = contact.name
            contact_phone.text = contact.phone
        }
    }
}