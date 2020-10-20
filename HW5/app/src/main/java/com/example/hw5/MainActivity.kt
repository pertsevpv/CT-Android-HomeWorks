package com.example.hw5

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

import kotlin.collections.ArrayList

const val CURRENT_FRAGMENT_KEY = "CURRENT_FRAGMENT_KEY"
const val STACK_KEY = "STACK_KEY"
const val ITEM_ID_KEY = "ITEM_ID_KEY"

class MainActivity : AppCompatActivity() {

    private var currentFragmentTag: String? = null
    private var stack: LinkedList<Int> = LinkedList()
    private var curItemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        Log.i("cur", "$curItemId")
        if (curItemId == null)
            curItemId = R.id.navigation_home

        setFragment(curItemId!!)

        Log.i("id1", R.id.navigation_home.toString())
        Log.i("id2", R.id.navigation_dashboard.toString())
        Log.i("id3", R.id.navigation_notifications.toString())

        main_bottom_navigation_view?.apply {
            setOnNavigationItemSelectedListener {
                addFragment(it.itemId)
                setFragment(it.itemId)
                true
            }
        }

        main_navigation_view?.setNavigationItemSelectedListener {
            addFragment(it.itemId)
            setFragment(it.itemId)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_FRAGMENT_KEY, currentFragmentTag)
        outState.putIntegerArrayList(STACK_KEY, ArrayList(stack))
        outState.putInt(ITEM_ID_KEY, curItemId!!)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_KEY)
        stack = LinkedList(savedInstanceState.getIntegerArrayList(STACK_KEY)!!)
        curItemId = savedInstanceState.getInt(ITEM_ID_KEY)
        main_bottom_navigation_view?.selectedItemId = curItemId!!
        setFragment(curItemId!!)
    }

    private fun addFragment(itemId: Int) {
        if (stack.contains(itemId)) stack.remove(itemId)
        stack.addLast(itemId)
    }

    private fun setFragment(itemId: Int) {
        curItemId = itemId
        val fragment = supportFragmentManager.findFragmentByTag("$itemId") ?: RootFragment()
        val transaction = supportFragmentManager.beginTransaction()

        Log.i("item", itemId.toString())
        Log.i("stack", stack.toString())
        val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (!fragment.isAdded) {
            transaction.add(R.id.main_fragment_container, fragment, "$itemId")
        } else {
            transaction.show(fragment)
        }
        currentFragmentTag = fragment.tag
        transaction.commit()
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag) ?: return
        if (fragment.childFragmentManager.backStackEntryCount == 0) {
            if (stack.isNotEmpty()) {
                val last = stack.last
                main_bottom_navigation_view?.selectedItemId = last
                setFragment(last)
                stack.remove(last)
            } else
                finish()

        } else {
            fragment.childFragmentManager.popBackStack()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
