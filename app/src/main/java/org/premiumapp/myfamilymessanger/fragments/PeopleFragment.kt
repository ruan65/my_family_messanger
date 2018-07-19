package org.premiumapp.myfamilymessanger.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ListenerRegistration

import org.premiumapp.myfamilymessanger.R

class PeopleFragment : Fragment() {

    private lateinit var userRegistrationListener: ListenerRegistration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_people, container, false)


        return root
    }
}
