package hr.ferit.josipnovak.projectrma.model

data class Event(
    var id: String = "",
    var clubId: String = "",
    val type: String ="",
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val location: Location = Location()
)
