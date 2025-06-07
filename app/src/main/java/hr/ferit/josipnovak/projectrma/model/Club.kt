package hr.ferit.josipnovak.projectrma.model

data class Club(
    val id: String = "",
    val name: String = "",
    val coachId: String = "",
    val players: List<String> = emptyList(),
    val clubCode: Int = 0
)
