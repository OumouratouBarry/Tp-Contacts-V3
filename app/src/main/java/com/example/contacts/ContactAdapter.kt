package com.example.contacts

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onContactClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgContact: ImageView = view.findViewById(R.id.imgContact)
        val txtNom: TextView = view.findViewById(R.id.txtNom)
        val txtPrenom: TextView = view.findViewById(R.id.txtPrenom)
        val txtTelephone: TextView = view.findViewById(R.id.txtTelephone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.txtNom.text = contact.nom
        holder.txtPrenom.text = contact.prenom
        holder.txtTelephone.text = contact.telephone

        chargerPhoto(holder.itemView.context, holder.imgContact, contact.photo)

        holder.itemView.setOnClickListener {
            onContactClick(contact)
        }
    }

    override fun getItemCount(): Int = contacts.size

    fun updateData(newContacts: List<Contact>) {
        this.contacts = newContacts
        notifyDataSetChanged()
    }

    companion object {
        fun chargerPhoto(context: Context, imageView: ImageView, photoPath: String) {
            if (photoPath.isNotEmpty()) {
                val resId = context.resources.getIdentifier(photoPath, "drawable", context.packageName)
                if (resId != 0) {
                    imageView.setImageResource(resId)
                    return
                }

                try {
                    val uri = Uri.parse(photoPath)
                    imageView.setImageURI(uri)

                    if (imageView.drawable == null) {
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap)
                            } else {
                                imageView.setImageResource(android.R.drawable.sym_def_app_icon)
                            }
                        }
                    }
                } catch (e: Exception) {
                    imageView.setImageResource(android.R.drawable.sym_def_app_icon)
                }
            } else {
                imageView.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        }
    }
}
