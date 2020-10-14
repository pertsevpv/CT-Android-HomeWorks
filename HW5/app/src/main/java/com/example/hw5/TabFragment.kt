package com.example.hw5

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.base_fragment.view.*

const val COUNT_KEY = "COUNT_KEY"

class TabFragment : Fragment() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        count = arguments?.getInt(COUNT_KEY) ?: 0
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.base_fragment, container, false)
        with(view) {
            navigation_fragment_text.text = count.toString()
            navigation_fragment_push_button.setOnClickListener {
                requireFragmentManager().apply {
                    beginTransaction()
                        .replace(R.id.empty_fragment_container, TabFragment().apply {
                            arguments = Bundle().apply {
                                putInt(COUNT_KEY, this@TabFragment.count + 1)
                            }
                        })
                        .addToBackStack("")
                        .commit()
                }
            }
        }
        return view
    }
}