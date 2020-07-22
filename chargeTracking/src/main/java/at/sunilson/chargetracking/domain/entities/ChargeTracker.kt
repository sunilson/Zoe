package at.sunilson.chargetracking.domain.entities

data class ChargeTracker(val id: String, val state: State) {

    enum class State {
        WAITING, WORKING, ERROR
    }
}