package com.example.hw5

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import kotlin.collections.ArrayList

const val CURRENT_FRAGMENT_KEY = "CURRENT_FRAGMENT_KEY"
const val STACK_KEY = "STACK_KEY"

class MainActivity : AppCompatActivity() {

    private var currentFragmentTag: String? = null
    private var stack: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_KEY)
            stack = savedInstanceState.getIntegerArrayList(STACK_KEY) as ArrayList<Int>
        } else {
            setFragment(R.id.navigation_home)
        }

        main_bottom_navigation_view?.setOnNavigationItemSelectedListener {
            setFragment(it.itemId)
            true
        }

        main_navigation_view?.setNavigationItemSelectedListener {
            setFragment(it.itemId)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_FRAGMENT_KEY, currentFragmentTag)
        outState.putIntegerArrayList(STACK_KEY, ArrayList(stack))
    }

    private fun setFragment(itemId: Int) {
        Log.i("stack", stack.toString())
        val fragment = supportFragmentManager.findFragmentByTag(itemId.toString()) ?: RootFragment()
        Log.i("frag", fragment.toString())
        val transaction = supportFragmentManager.beginTransaction()

        val currentFragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (!fragment.isAdded) {
            transaction.add(R.id.main_fragment_container, fragment, "$itemId")
            stack.add(itemId)
        } else {
            transaction.show(fragment)
        }

        currentFragmentTag = fragment.tag
        transaction.commit()
    }

    @ExperimentalStdlibApi
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag) ?: return
        if (fragment.childFragmentManager.backStackEntryCount == 0) {
            if (stack.isNotEmpty()) {
                val last = stack.removeLast()
                main_bottom_navigation_view?.selectedItemId = last
                setFragment(last)
            }
        } else {
            fragment.childFragmentManager.popBackStack()
        }
    }

    @ExperimentalStdlibApi
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
