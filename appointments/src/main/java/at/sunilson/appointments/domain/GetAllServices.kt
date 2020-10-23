package at.sunilson.appointments.domain

import at.sunilson.appointments.data.ServicesDao
import at.sunilson.appointments.domain.entities.Service
import at.sunilson.core.usecases.FlowUseCase
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

internal class GetAllServices @Inject constructor() : FlowUseCase<List<Service>, String>() {

    @Inject
    internal lateinit var servicesDao: ServicesDao

    override fun run(params: String) =
        servicesDao
            .getServices(params)
            .map { databaseServices ->
                databaseServices.map { dbService ->
                    Service(
                        try {
                            LocalDate.parse(dbService.date)
                        } catch (exception: Exception) {
                            LocalDate.now().minusDays(1)
                        }
                    )
                }.sortedBy { it.date }
            }
}