package org.premiumapp.myfamilymessanger.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.premiumapp.myfamilymessanger.model.User

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().uid
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

    fun getCurrentUser(onComplete: (User) -> Unit) {

        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java))
                }
    }
}