package at.sunilson.appointments.data

import at.sunilson.appointments.data.models.database.DatabaseAppointment
import at.sunilson.appointments.data.models.database.DatabaseService
import at.sunilson.appointments.data.models.network.NetworkAppointment
import at.sunilson.appointments.data.models.network.NetworkService

internal fun NetworkAppointment.toDatabaseEntity(vin: String) =
    DatabaseAppointment("$vin$code$date", label, date, vin)

internal fun NetworkService.toDatabaseEntity(vin: String) =
    DatabaseService("$vin${title.code}${subTitle.code}$date", date, vin)
