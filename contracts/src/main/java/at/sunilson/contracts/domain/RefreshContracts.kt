package at.sunilson.contracts.domain

import at.sunilson.contracts.data.ContractsDao
import at.sunilson.contracts.data.ContractsService
import at.sunilson.contracts.data.toDatabaseEntity
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class RefreshContracts @Inject constructor(private val vehicleCoreRepository: VehicleCoreRepository) :
    AsyncUseCase<Unit, String>() {

    @Inject
    internal lateinit var contractsService: ContractsService

    @Inject
    internal lateinit var contractsDao: ContractsDao

    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        val contracts =
            contractsService.getContracts(vehicleCoreRepository.kamereonAccountID, params)
        contractsDao.insertContracts(contracts.map { it.toDatabaseEntity(params) })
    }
}