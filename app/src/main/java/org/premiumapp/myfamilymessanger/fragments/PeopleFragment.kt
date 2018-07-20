package org.premiumapp.myfamilymessanger.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*

import org.premiumapp.myfamilymessanger.R
import org.premiumapp.myfamilymessanger.utils.FirestoreUtil

class PeopleFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true
    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        userListenerRegistration = FirestoreUtil.addUsersListener(this.activity!!, this::updateRecyclerView)

        val root = inflater.inflate(R.layout.fragment_people, container, false)


        return root
    }

    override fun onDestroyView() {
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
        super.onDestroyView()
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {

            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() {

        }

        if (shouldInitRecyclerView) {
            init()
        } else {
            updateItems()
        }

    }
}


