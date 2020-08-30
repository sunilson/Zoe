package at.sunilson.appointments.data

import at.sunilson.appointments.data.models.AppointmentResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface AppointmentsService {
    @GET("accounts/{accountId}/vehicles/{vin}/details?staticMaintenancePlan=true&country=AT&lang=de&mileageUnit=KM")
    suspend fun getAppointments(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): AppointmentResponse
}