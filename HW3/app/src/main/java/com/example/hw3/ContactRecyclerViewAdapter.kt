package com.example.hw3

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactRecyclerViewAdapter(val contactList: List<Contact>, val onClickText: (Contact) -> Unit,val onClickImage: (Contact) -> Unit) :
    RecyclerView.Adapter<ContactRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ContactRecyclerViewHolder {
        val holder = ContactRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contact_list_item,
                parent,
                false
            )
        )
        holder.root.contact_name.setOnClickListener {
            onClickText(contactList[holder.adapterPosition])
        }
        holder.root.contact_send.setOnClickListener {
            onClickImage(contactList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ContactRecyclerViewHolder, position: Int) =
        holder.bind(contactList[position])

    override fun getItemCount() =
        contactList.size

}