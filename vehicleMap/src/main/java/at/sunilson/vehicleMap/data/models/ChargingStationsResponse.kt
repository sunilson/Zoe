package at.sunilson.vehicleMap.data.models

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ChargingStationsResponse(
    @Json(name = "AddressInfo") val addressInfo: AddressInfo,
    @Json(name = "Connections") val connections: List<Connection>,
    @Json(name = "DataProvider") val dataProvider: DataProvider,
    @Json(name = "DataProviderID") val dataProviderID: Int,
    @Json(name = "DataQualityLevel") val dataQualityLevel: Int,
    @Json(name = "DateCreated") val dateCreated: String,
    @Json(name = "DateLastStatusUpdate") val dateLastStatusUpdate: String,
    @Json(name = "GeneralComments") val generalComments: String,
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsRecentlyVerified") val isRecentlyVerified: Boolean,
    @Json(name = "NumberOfPoints") val numberOfPoints: Int,
    @Json(name = "OperatorID") val operatorID: Int,
    @Json(name = "OperatorInfo") val operatorInfo: OperatorInfo,
    @Json(name = "OperatorsReference") val operatorsReference: String,
    @Json(name = "StatusType") val statusType: StatusTypeX,
    @Json(name = "StatusTypeID") val statusTypeID: Int,
    @Json(name = "SubmissionStatus") val submissionStatus: SubmissionStatus,
    @Json(name = "SubmissionStatusTypeID") val submissionStatusTypeID: Int,
    @Json(name = "UUID") val uUID: String,
    @Json(name = "UsageType") val usageType: UsageType,
    @Json(name = "UsageTypeID") val usageTypeID: Int
)

@Keep
@JsonClass(generateAdapter = true)
data class AddressInfo(
    @Json(name = "AddressLine1") val addressLine1: String,
    @Json(name = "ContactTelephone1") val contactTelephone1: String,
    @Json(name = "Country") val country: Country,
    @Json(name = "CountryID") val countryID: Int,
    @Json(name = "Distance") val distance: Double,
    @Json(name = "DistanceUnit") val distanceUnit: Int,
    @Json(name = "ID") val iD: Int,
    @Json(name = "Latitude") val latitude: Double,
    @Json(name = "Longitude") val longitude: Double,
    @Json(name = "Postcode") val postcode: String,
    @Json(name = "Title") val title: String,
    @Json(name = "Town") val town: String
)

@Keep
@JsonClass(generateAdapter = true)
data class Connection(
    @Json(name = "Amps") val amps: Int,
    @Json(name = "ConnectionType") val connectionType: ConnectionType,
    @Json(name = "ConnectionTypeID") val connectionTypeID: Int,
    @Json(name = "CurrentType") val currentType: CurrentType,
    @Json(name = "CurrentTypeID") val currentTypeID: Int,
    @Json(name = "ID") val iD: Int,
    @Json(name = "Level") val level: Level,
    @Json(name = "LevelID") val levelID: Int,
    @Json(name = "PowerKW") val powerKW: Int,
    @Json(name = "Quantity") val quantity: Int,
    @Json(name = "StatusType") val statusType: StatusType,
    @Json(name = "StatusTypeID") val statusTypeID: Int,
    @Json(name = "Voltage") val voltage: Int
)

@Keep
@JsonClass(generateAdapter = true)
data class DataProvider(
    @Json(name = "DataProviderStatusType") val dataProviderStatusType: DataProviderStatusType,
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsApprovedImport") val isApprovedImport: Boolean,
    @Json(name = "IsOpenDataLicensed") val isOpenDataLicensed: Boolean,
    @Json(name = "IsRestrictedEdit") val isRestrictedEdit: Boolean,
    @Json(name = "License") val license: String,
    @Json(name = "Title") val title: String,
    @Json(name = "WebsiteURL") val websiteURL: String
)

@Keep
@JsonClass(generateAdapter = true)
data class OperatorInfo(
    @Json(name = "ID") val iD: Int,
    @Json(name = "Title") val title: String,
    @Json(name = "WebsiteURL") val websiteURL: String
)

@Keep
@JsonClass(generateAdapter = true)
data class StatusTypeX(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsOperational") val isOperational: Boolean,
    @Json(name = "IsUserSelectable") val isUserSelectable: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class SubmissionStatus(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsLive") val isLive: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class UsageType(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsAccessKeyRequired") val isAccessKeyRequired: Boolean,
    @Json(name = "IsMembershipRequired") val isMembershipRequired: Boolean,
    @Json(name = "IsPayAtLocation") val isPayAtLocation: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "ContinentCode") val continentCode: String,
    @Json(name = "ID") val iD: Int,
    @Json(name = "ISOCode") val iSOCode: String,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class ConnectionType(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsDiscontinued") val isDiscontinued: Boolean,
    @Json(name = "IsObsolete") val isObsolete: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class CurrentType(
    @Json(name = "Description") val description: String,
    @Json(name = "ID") val iD: Int,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class Level(
    @Json(name = "Comments") val comments: String,
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsFastChargeCapable") val isFastChargeCapable: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class StatusType(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsOperational") val isOperational: Boolean,
    @Json(name = "IsUserSelectable") val isUserSelectable: Boolean,
    @Json(name = "Title") val title: String
)

@Keep
@JsonClass(generateAdapter = true)
data class DataProviderStatusType(
    @Json(name = "ID") val iD: Int,
    @Json(name = "IsProviderEnabled") val isProviderEnabled: Boolean,
    @Json(name = "Title") val title: String
)