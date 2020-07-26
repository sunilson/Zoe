package at.sunilson.chargetracking.domain.entities

data class ChargeTracker(val vin: String, val state: State) {
    enum class State {
        WAITING, WORKING, COMPLETED
    }
}

val ChargeTracker?.isTracking: Boolean
    get() = this != null && this.state != ChargeTracker.State.COMPLETED