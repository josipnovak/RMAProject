package hr.ferit.josipnovak.projectrma.model

import com.google.firebase.Timestamp

data class Event(
    var id: String = "",
    var clubId: String = "",
    val type: String ="",
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val createdAt: Timestamp? = null,
    val location: Location = Location()
)
