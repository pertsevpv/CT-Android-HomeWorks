package com.example.hw8

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
const val DATABASE_NAME = "post_database"
const val TABLE_NAME = "fake_posts"
const val FIRST_LAUNCH_KEY = "FIRST_LAUNCH_KEY"
const val MAIN_PREFS_NAME = "MAIN_PREFS_NAME"

enum class QueryTypes {
    POST, DELETE
}

class MainActivity : AppCompatActivity() {

    var postList: ArrayList<Post> = arrayListOf()

    private lateinit var listAdapter: PostRecyclerViewAdapter

    private var getAllTask: DBGetAllTask? = null
    private var insertTask: DBInsertTask? = null
    private var deleteAllTask: DBClearTask? = null
    private var deleteTask: DBDeleteTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState != null) {
            postList =
                savedInstanceState.getParcelableArrayList<Post>(POST_LIST_KEY) as ArrayList<Post>
        }

        if (postList.isNullOrEmpty())
            getAllPostsFromBD()

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

    fun getAllPosts() {
        progressBar.visibility = ProgressBar.VISIBLE
        ThisApp.instance.fakeAPIService.getAllPosts().enqueue(PostListCallback())
    }

    fun getPostsByUserId(id: Int) {
        progressBar.visibility = ProgressBar.VISIBLE
        ThisApp.instance.fakeAPIService.getPostByUserId(id).enqueue(PostListCallback())
    }

    private fun postNewPost(data: Post) {
        ThisApp.instance.fakeAPIService.loadNewPost(data)
            .enqueue(PostCallback(QueryTypes.POST, data))
    }

    private fun deletePost(post: Post) {
        progressBar.visibility = ProgressBar.VISIBLE
        ThisApp.instance.fakeAPIService.deletePostById(post.userId)
            .enqueue(PostCallback(QueryTypes.DELETE, post))
    }

    private fun getAllPostsFromBD() {
        getAllTask?.cancel(true)
        getAllTask = DBGetAllTask(this)
        getAllTask?.execute()
    }

    private fun insertPostInDatabase(vararg post: Post) {
        progressBar.visibility = ProgressBar.VISIBLE
        insertTask?.cancel(true)
        insertTask = DBInsertTask(this)
        insertTask?.execute(*post)
    }

    private fun reloadAPI() {
        progressBar.visibility = ProgressBar.VISIBLE
        deleteAllTask?.cancel(true)
        deleteAllTask = DBClearTask(this)
        deleteAllTask?.execute()
    }

    private fun deletePostFromDB(post: Post) {
        progressBar.visibility = ProgressBar.VISIBLE
        deleteTask?.cancel(true)
        deleteTask = DBDeleteTask(this)
        deleteTask?.execute(post)
    }


    fun update() {
        main_recycler.recycledViewPool.clear()
        listAdapter.notifyDataSetChanged()
    }

    fun showAlert(msg: String) {
        progressBar.visibility = ProgressBar.INVISIBLE
        Snackbar.make(main_layout, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(POST_LIST_KEY, postList)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        postList = savedInstanceState.getParcelableArrayList<Post>(POST_LIST_KEY) as ArrayList<Post>
        progressBar.visibility = ProgressBar.INVISIBLE
        update()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> when (resultCode) {
                2 -> {
                    val newPost: Post = (data?.getParcelableExtra(NEW_POST_KEY) ?: return) ?: return
                    postNewPost(newPost)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                getAllPostsFromBD()
                return true
            }
            R.id.action_reload -> {
                reloadAPI()
                return true
            }
            else -> false
        }
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
            /*for (post in postList)*/ insertPostInDatabase(*postList.toTypedArray())
            update()
            showAlert("${resources.getString(R.string.api_posts)} \n${response.code()}")
        }

        override fun onFailure(call: Call<List<Post>>, t: Throwable) {
            showAlert(
                "${resources.getString(R.string.fail)}\n${t.localizedMessage ?: resources.getString(
                    R.string.bad_connection
                )}"
            )
        }
    }

    inner class PostCallback(private val type: QueryTypes, private val post: Post = Post()) :
        Callback<Post> {

        override fun onResponse(call: Call<Post>, response: Response<Post>) {
            if (response.body() == null) {
                showAlert("${resources.getString(R.string.bad_connection)} \n${response.code()}")
                return
            }
            val result = response.body()
            when (type) {
                QueryTypes.POST -> {
                    result?.postId = postList.size * 2 + 1
                    postList.add(result!!)
                    insertPostInDatabase(result)
                    showAlert("${resources.getString(R.string.new_posts)} \n${response.code()}")
                }
                QueryTypes.DELETE -> {
                    if (postList.contains(post)) {
                        postList.remove(post)
                        deletePostFromDB(post)
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
            when (type) {
                QueryTypes.POST -> {
                    post.postId = postList.size * 2 + 1
                    postList.add(post)
                    insertPostInDatabase(post)
                }
                QueryTypes.DELETE -> {
                    if (postList.contains(post)) {
                        postList.remove(post)
                        deletePostFromDB(post)
                    }
                }
            }
            update()
        }
    }
}
