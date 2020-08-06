package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.remote.ChargeSettingsResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface ChargeScheduleService {
    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/charging-settings?country=AT")
    suspend fun getChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ChargeSettingsResponse
}