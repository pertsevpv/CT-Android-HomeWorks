package com.example.hw4

import android.graphics.Bitmap
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.image_card.view.*

@Parcelize
data class ImageCard(
    var description: String = "",
    var previewBitmap: Bitmap? = null,
    var fullURL: String = ""
):Parcelable

class ImageCardHolder(val root: View) : RecyclerView.ViewHolder(root) {
    fun bind(img: ImageCard) {
        with(root) {
            if (img.description.isNullOrBlank())
                description.setText(R.string.no_description)
            else
                description.text = img.description
            imagePreview.setImageBitmap(img.previewBitmap)
        }
    }
}

class ImageCardRecyclerViewAdapter(
    private val imageList: List<ImageCard>,
    private val onClick: ((ImageCard) -> Unit)
) : RecyclerView.Adapter<ImageCardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardHolder {
        val holder = ImageCardHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_card,
                parent,
                false
            )
        )
        holder.root.card_item.setOnClickListener {
            onClick(imageList[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageCardHolder, position: Int) =
        holder.bind(imageList[position])
}
