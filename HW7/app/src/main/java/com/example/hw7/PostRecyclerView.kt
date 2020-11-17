package com.example.hw7

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.post_item.view.*

@Parcelize
data class Post(
    @field:Json(name = "userId")var userId: Int = 0,
    @field:Json(name = "id")var postId: Int = 0,
    @field:Json(name = "title")var postTitle: String = "",
    @field:Json(name = "body")var postBody: String = ""
) : Parcelable

class PostHolder(val root: View) : RecyclerView.ViewHolder(root) {
    fun bind(post: Post) {
        with(root) {
            title.text = post.postTitle
            body.text = post.postBody
        }
    }
}

class PostRecyclerViewAdapter(
    private val postList: List<Post>,
    private val onClick: ((Post) -> Unit)
) : RecyclerView.Adapter<PostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val holder = PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.post_item,
                parent,
                false
            )
        )
        holder.root.delete_button.setOnClickListener {
            onClick(postList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) =
        holder.bind(postList[position])

    override fun getItemCount() = postList.size

}