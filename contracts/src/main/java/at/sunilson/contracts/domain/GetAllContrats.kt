package at.sunilson.contracts.domain

import at.sunilson.contracts.data.ContractsDao
import at.sunilson.contracts.data.toEntity
import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetAllContrats @Inject constructor(private val contractsDao: ContractsDao) :
    FlowUseCase<List<Contract>, String>() {
    override fun run(params: String) =
        contractsDao.getAllContracts(params)
            .map { contracts ->
                contracts
                    .map { databaseContract -> databaseContract.toEntity() }
                    .sortedWith(compareBy<Contract> { it.endDate }.thenBy { it.id })
            }
}
