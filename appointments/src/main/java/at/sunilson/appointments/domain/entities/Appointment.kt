package at.sunilson.appointments.domain.entities

import java.time.LocalDate

data class Appointment(
    val date: LocalDate?,
    val label: String
) {
    val upcoming: Boolean
        get() = date?.isAfter(LocalDate.now()) == true
}