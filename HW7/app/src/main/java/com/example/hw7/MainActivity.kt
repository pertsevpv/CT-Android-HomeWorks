package com.example.hw7

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE

import kotlin.collections.ArrayList
import kotlin.random.Random


const val NEW_POST_KEY = "NEW_POST_KEY"
const val POST_LIST_KEY = "POST_LIST_KEY"
const val MY_USER_ID = 1
const val NEW_POST_REQUEST_CODE = 1
const val NEW_POST_RESULT_CODE = 2

enum class queryTypes {
    POST, DELETE
}

class MainActivity : AppCompatActivity() {

    private var postList: ArrayList<Post> = arrayListOf()

    private lateinit var listAdapter: PostRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            postList =
                savedInstanceState.getParcelableArrayList<Post>(POST_LIST_KEY) as ArrayList<Post>
        }
        if (postList.isNullOrEmpty()) getPostsByUserId(MY_USER_ID)
        else {
            progressBar.visibility = ProgressBar.INVISIBLE
        }

        listAdapter = PostRecyclerViewAdapter(postList) {
            deletePost(it)
        }
        main_recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }
        update()
        add_button.setOnClickListener {
            startActivityForResult(
                Intent(this@MainActivity, AddActivity::class.java),
                NEW_POST_REQUEST_CODE
            )
        }
    }

    private fun getAllPosts() {
        ThisApp.instance.fakeAPIService.getAllPosts().enqueue(PostListCallback())
    }

    private fun getPostsByUserId(id: Int) {
        ThisApp.instance.fakeAPIService.getPostByUserId(id).enqueue(PostListCallback())
    }

    private fun postNewPost(data: Post) {
        ThisApp.instance.fakeAPIService.loadNewPost(data).enqueue(PostCallback(queryTypes.POST))
    }

    private fun deletePost(post: Post) {
        ThisApp.instance.fakeAPIService.deletePostById(post.userId)
            .enqueue(PostCallback(queryTypes.DELETE, post))
    }

    private fun update() {
        listAdapter.notifyDataSetChanged()
    }

    private fun showAlert(msg: String) {
        progressBar.visibility = ProgressBar.INVISIBLE
        AlertDialog.Builder(this).apply {
            setMessage(msg)
            setPositiveButton(resources.getString(R.string.ok)) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        }.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(POST_LIST_KEY, postList)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        postList = savedInstanceState.getParcelableArrayList<Post>(POST_LIST_KEY) as ArrayList<Post>
        update()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> when (resultCode) {
                2 -> {
                    val newPost: Post = (data?.getParcelableExtra(NEW_POST_KEY) ?: return) ?: return
                    Log.i("new one", newPost.toString())
                    postNewPost(newPost)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class PostListCallback : Callback<List<Post>> {
        override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
            if (response.body() == null) {
                showAlert("${resources.getString(R.string.bad_connection)} \n${response.code()}")
                return
            }
            val result = response.body()
            postList.clear()
            postList.addAll(result as ArrayList)
            listAdapter.notifyDataSetChanged()
            showAlert("${resources.getString(R.string.new_posts)} \n${response.code()}")
        }

        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            showAlert(
                "${resources.getString(R.string.fail)}\n${t.localizedMessage ?: resources.getString(
                    R.string.bad_connection
                )}"
            )
        }
    }

    inner class PostCallback(private val type: queryTypes, private val post: Post = Post()) :
        Callback<Post> {

        override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (response.body() == null) {
                showAlert("${resources.getString(R.string.bad_connection)} \n${response.code()}")
                return
            }
            val result = response.body()
            when (type) {
                queryTypes.POST -> {
                    postList.add(result!!)
                    showAlert("${resources.getString(R.string.new_posts)} \n${response.code()}")
                }
                queryTypes.DELETE -> {
                    if (postList.contains(post)) {
                        postList.remove(post)
                        showAlert("${resources.getString(R.string.del_posts)} \n${response.code()}")
                    }
                }
            }
            update()
        }

        override fun onFailure(call: Call<Post>, t: Throwable) {
            showAlert(
                "${resources.getString(R.string.fail)}\n${t.localizedMessage ?: resources.getString(
                    R.string.bad_connection
                )}"
            )
        }

    }
}
