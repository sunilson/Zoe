package at.sunilson.vehiclecore.domain

import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.data.VehicleDao
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class TestUseCase @Inject constructor(private val vehicleDao: VehicleDao) :
    AsyncUseCase<Unit, Unit>() {
    override suspend fun run(params: Unit): SuspendableResult<Unit, Exception> {
        TODO("Not yet implemented")
    }
}