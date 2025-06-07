package hr.ferit.josipnovak.projectrma.model

import androidx.compose.ui.semantics.Role

data class User(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: String = "",
    val clubId: String =  "",
    val goals: Int = 0,
    val assists: Int = 0,
    val matches: Int = 0,
    val trainings: Int = 0,
    val position: String = "",
)
