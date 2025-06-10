package hr.ferit.josipnovak.projectrma.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.josipnovak.projectrma.FirebaseAuth
import hr.ferit.josipnovak.projectrma.model.Club
import hr.ferit.josipnovak.projectrma.model.User

class AccountDetailsViewModel(
    private val fbAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    fun getUserAndClubData(callback: (User, Club) -> Unit) {
        val currentUser = fbAuth.getCurrentUser()
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val user = documents.first()
                            val clubId = user.getString("clubId") ?: "No Club ID"
                            db.collection("clubs").document(clubId)
                                .get()
                                .addOnSuccessListener { clubDocument ->
                                    val club = clubDocument.toObject(Club::class.java) ?: Club()
                                    club.id = clubDocument.id
                                    val userObj = user.toObject(User::class.java) ?: User()
                                    userObj.id = user.id
                                    callback(userObj, club)
                                }
                        }
                    }
            }
        }
        callback(User(), Club())
    }

    fun logout() {
        fbAuth.logoutUser()
    }
}