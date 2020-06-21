package at.sunilson.database.mappers

import at.sunilson.entities.Vehicle

fun Vehicle.toDatabaseEntity() = at.sunilson.database.databaseentities.DatabaseVehicle(vin, this)