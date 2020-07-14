package at.sunilson.vehicle.domain

internal interface VehicleRepository {
    var selectedVehicle: String?
    val kamereonAccountID: String
}