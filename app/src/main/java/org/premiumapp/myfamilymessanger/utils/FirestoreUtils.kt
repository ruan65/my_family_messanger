package org.premiumapp.myfamilymessanger.utils

import android.content.Context
import android.util.Log.e
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import org.premiumapp.myfamilymessanger.model.User
import org.premiumapp.myfamilymessanger.recycler.items.PersonItem

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null")}")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                        FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", null)

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else onComplete()
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "", profilePicturePath: String? = null) {

        val userFieldsMap = mutableMapOf<String, Any>()

        if (name.isNotBlank()) userFieldsMap["name"] = name
        if (bio.isNotBlank()) userFieldsMap["bio"] = bio

        if (profilePicturePath != null) userFieldsMap["profilePicturePath"] = profilePicturePath

        currentUserDocRef.update(userFieldsMap)
    }

    fun getCurrentUser(onComplete: (User?) -> Unit) {

        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java))
                }
    }

    fun addUsersListener(ctx: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {

        return firestoreInstance.collection("users")
                .addSnapshotListener{querySnapshot, firebaseFirestoreException ->

                    if (firebaseFirestoreException != null) {
                        e("Firestore", "Users listener error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()

                    querySnapshot.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                            items.add(PersonItem(it.toObject(User::class.java), it.id, ctx))
                        }

                        onListen(items)
                    }
                }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()
}