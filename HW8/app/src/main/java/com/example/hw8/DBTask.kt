package com.example.hw8

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*

class DBGetAllTask(private val activity: MainActivity) : AsyncTask<Unit, Unit, List<Post>>() {

    @SuppressLint("CommitPrefEdits")
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
            if (!activity.getSharedPreferences(MAIN_PREFS_NAME, Context.MODE_PRIVATE)
                    .contains(FIRST_LAUNCH_KEY)
            ) {
                activity.getSharedPreferences(MAIN_PREFS_NAME, Context.MODE_PRIVATE).edit()
                    .putBoolean(
                        FIRST_LAUNCH_KEY, true
                    ).apply()
                activity.getAllPosts()
            } else {
                activity.showAlert(activity.resources.getString(R.string.bd_empty))
                activity.progressBar.visibility = ProgressBar.INVISIBLE
            }
        }
        activity.update()
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
            getAllPosts()
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
            update()
        }
    }

    override fun doInBackground(vararg params: Post?) {
        for (post in params)
            if (post != null) {
                ThisApp.instance.postDB.getFakeDao().insertPost(post)

            }
    }
}

class DBDeleteTask(private val activity: MainActivity) : AsyncTask<Post, Unit, Unit>() {

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        activity.apply {
            progressBar.visibility = ProgressBar.INVISIBLE
            update()
        }
    }

    override fun doInBackground(vararg params: Post?) {
        for (post in params)
            if (post != null)
                ThisApp.instance.postDB.getFakeDao().deletePost(post)

    }
}