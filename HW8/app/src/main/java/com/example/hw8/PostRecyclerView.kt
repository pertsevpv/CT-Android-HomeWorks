package com.example.hw8

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.post_item.view.*


@Parcelize
@Entity(tableName = TABLE_NAME)
data class Post(
    @ColumnInfo(name = "user_id") @field:Json(name = "userId") var userId: Int = 0,
    @PrimaryKey @field:Json(name = "id") var postId: Int = 0,
    @ColumnInfo(name = "title_text") @field:Json(name = "title") var postTitle: String = "",
    @ColumnInfo(name = "body_text") @field:Json(name = "body") var postBody: String = ""
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