package at.sunilson.appointments.data

import at.sunilson.appointments.data.models.network.AppointmentResponse
import at.sunilson.appointments.data.models.network.ServiceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface AppointmentsService {
    @GET("accounts/{accountId}/vehicles/{vin}/dynamic-maintenance-plan?&country=AT&lang=de&brand=RENAULT&flagSave=false")
    suspend fun getAppointments(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String,
        @Query("annualMileage") annualMileage: Int,
        @Query("mileageDate") mileageDate: String
    ): AppointmentResponse

    @GET("accounts/{accountId}/vehicles/{vin}/service-history?country=AT&lang=de&brand=RENAULT")
    suspend fun getServiceHistory(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ServiceResponse
}