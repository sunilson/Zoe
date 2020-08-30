package at.sunilson.appointments.data

import at.sunilson.appointments.data.models.DatabaseAppointment
import at.sunilson.appointments.data.models.NetworkAppointment

internal fun NetworkAppointment.toDatabaseEntity(vin: String) =
    DatabaseAppointment("$vin$code$date", label, date, vin)