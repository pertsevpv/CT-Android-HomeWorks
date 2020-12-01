package com.example.hw8

import android.os.AsyncTask
import android.util.Log
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class DBGetAllTask(private val activity: MainActivity) : AsyncTask<Unit, Unit, List<Post>>() {

    override fun onPostExecute(result: List<Post>?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {
            activity.apply {
                postList.clear()
                postList.addAll(result)
                update()
                showAlert(activity.resources.getString(R.string.bd_posts))
            }
        } else {
            activity.getAllPosts()
        }
    }

    override fun doInBackground(vararg params: Unit?): List<Post>? {
        return ThisApp.instance.postDB.getFakeDao().getAll()
    }

}

class DBClearTask(private val activity: MainActivity) : AsyncTask<Unit, Unit, Unit>() {

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        activity.apply {
            postList.clear()
            progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun doInBackground(vararg params: Unit?) {
        ThisApp.instance.postDB.getFakeDao().deleteAll()
    }
}

class DBInsertTask(private val activity: MainActivity) : AsyncTask<Post, Unit, Unit>() {

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        activity.apply {
            progressBar.visibility = ProgressBar.INVISIBLE
        }
    }

    override fun doInBackground(vararg params: Post?) {
        for (post in params)
            if (post != null)
                ThisApp.instance.postDB.getFakeDao().insertPost(post)
    }
}

class DBDeleteTask(private val activity: MainActivity) : AsyncTask<Post, Unit, Unit>() {

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        activity.apply {
            progressBar.visibility = ProgressBar.INVISIBLE
            showAlert(resources.getString(R.string.bd_del))
        }
    }

    override fun doInBackground(vararg params: Post?) {
        for (post in params)
            if (post != null)
                ThisApp.instance.postDB.getFakeDao().deletePost(post)
    }
}