package com.example.hw4


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val API_URL = "https://api.unsplash.com/photos/?"
        private const val ACCESS_KEY = "lZXmGU_IMMD2yxTAiaXYrSFdmsIu5HDx9OMxtVWNjMU"
        private const val PER_PAGE = 30
        private var PAGE = 1

        val mainlist: ArrayList<ImageCard> = arrayListOf()

    }

    private var asyncTask: ImageLoader? = null
    private lateinit var listAdapter: ImageCardRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listAdapter = ImageCardRecyclerViewAdapter(mainlist) {
            startActivity(Intent(this@MainActivity, HQImageActivity::class.java).apply {
                putExtra(DESCRIPTION_KEY, it.description)
                putExtra(FULLURL_KEY, it.fullURL)
            })
        }
        image_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }

        if (mainlist.isNullOrEmpty()) executeQuery()
        showList()
        more_button.setOnClickListener {
            PAGE++
            executeQuery()
            showList()
        }
    }

    private fun makeQueryUrl(): String =
        "${API_URL}page=${PAGE}&per_page=${PER_PAGE}&client_id=$ACCESS_KEY"

    private fun executeQuery() {
        asyncTask = ImageLoader(this)
        asyncTask?.execute(makeQueryUrl())
    }

    fun showList() {
        listAdapter.notifyDataSetChanged()
    }

    fun addToList(result: List<ImageCard>) {
        mainlist.addAll(result)
    }

}
