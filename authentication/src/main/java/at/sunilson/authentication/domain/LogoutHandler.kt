package at.sunilson.authentication.domain

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LogoutHandler {
    val loggedOutEvent: Flow<Unit>
    fun emitLogout()
}

internal class LogoutHandlerImpl @Inject constructor() : LogoutHandler {

    private val eventsChannel = MutableSharedFlow<Unit>()

    override val loggedOutEvent: Flow<Unit>
        get() = eventsChannel.asSharedFlow()

    @OptIn(DelicateCoroutinesApi::class)
    override fun emitLogout() {
        GlobalScope.launch {
            eventsChannel.emit(Unit)
        }
    }
}
