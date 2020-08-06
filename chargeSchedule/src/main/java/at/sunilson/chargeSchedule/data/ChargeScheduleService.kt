package at.sunilson.chargeSchedule.data

import at.sunilson.chargeSchedule.data.models.ChargeSettingsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChargeScheduleService {
    @GET("accounts/{accountId}/kamereon/kca/car-adapter/v1/cars/{vin}/charge-settings?country=AT")
    suspend fun getChargingSchedule(
        @Path("accountId") accountId: String,
        @Path("vin") vin: String
    ): ChargeSettingsResponse
}