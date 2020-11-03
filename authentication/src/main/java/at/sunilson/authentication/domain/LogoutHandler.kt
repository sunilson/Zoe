package at.sunilson.authentication.domain

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

interface LogoutHandler {
    val loggedOutEvent: Flow<Unit>
    fun emitLogout()
}

internal class LogoutHandlerImpl @Inject constructor() : LogoutHandler {

    private val eventsChannel = BroadcastChannel<Unit>(1)

    override val loggedOutEvent: Flow<Unit>
        get() = eventsChannel.asFlow()

    override fun emitLogout() {
        eventsChannel.offer(Unit)
    }
}