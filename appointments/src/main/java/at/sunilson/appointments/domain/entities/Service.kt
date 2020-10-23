package at.sunilson.appointments.domain.entities

import java.time.LocalDate

data class Service(
    val date: LocalDate,
    val label: String = "Wartung"
)