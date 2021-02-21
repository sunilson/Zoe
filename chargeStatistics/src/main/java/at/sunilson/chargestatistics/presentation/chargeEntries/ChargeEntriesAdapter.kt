package at.sunilson.chargestatistics.presentation.chargeEntries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import at.sunilson.chargestatistics.databinding.ChargeTrackingPointEntryItemBinding
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.core.extensions.isSameDay
import at.sunilson.presentationcore.extensions.formatPattern

class ChargeEntriesDiffCallback : DiffUtil.ItemCallback<ChargingProcedure>() {
    override fun areContentsTheSame(
        oldItem: ChargingProcedure,
        newItem: ChargingProcedure
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: ChargingProcedure,
        newItem: ChargingProcedure
    ): Boolean {
        return oldItem.startTime == newItem.startTime
    }
}

class ChargeEntriesAdapter :
    PagingDataAdapter<ChargingProcedure, ChargeEntriesAdapter.ViewHolder>(ChargeEntriesDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), if (position != 0) getItem(position - 1) else null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChargeTrackingPointEntryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: ChargeTrackingPointEntryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChargingProcedure?, lastProcedure: ChargingProcedure?) {

            if (item == null) return

            if (lastProcedure?.startTime?.isSameDay(item.startTime) != true) {
                binding.sectionHeader.isVisible = true
                binding.sectionHeader.text = item.startTime.formatPattern("dd.MM.YYYY")
            } else {
                binding.sectionHeader.isVisible = false
            }

            binding.chargeProcedureBattery.text =
                "${item.batteryLevelDifference} % geladen (${item?.startBatteryLevel} --> ${item?.endBatteryLevel})"
            binding.chargeProcedureEnergy.text = "${item?.energyLevelDifference} kWh"
            binding.chargeProcedureFrom.text =
                "${item?.startTime?.formatPattern("dd.MM HH:mm")} bis ${
                    item?.endTime?.formatPattern("dd.MM HH:mm")
                }"
        }
    }
}
