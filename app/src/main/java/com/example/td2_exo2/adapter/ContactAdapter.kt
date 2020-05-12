package com.example.td2_exo2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.td2_exo2.entity.Contact
import com.example.td2_exo2.R


class ContactAdapter: RecyclerView.Adapter<ContactAdapter.ContactHolder>() {




    companion object{
        private var contactList:List<Contact> =ArrayList()

    }



    class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val  text_view_nom: TextView =itemView.findViewById(R.id.item_nom)
        internal val  text_view_num: TextView =itemView.findViewById(R.id.item_num)
        internal val  text_view_mail: TextView =itemView.findViewById(R.id.item_mail)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item,parent,false)

        return ContactHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun setContact(contactListt:List<Contact>)
    {
        contactList =contactListt
        notifyDataSetChanged()
    }

    fun getContactAt(position: Int): Contact? {
        return contactList[position]
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact: Contact =
            contactList.get(position)
        holder.text_view_nom.setText(currentContact.nom)
        holder.text_view_num.setText(currentContact.num)
        holder.text_view_mail.setText(currentContact.mail)


    }

}