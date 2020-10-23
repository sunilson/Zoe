package at.sunilson.appointments.presentation.history

import android.widget.TextView
import androidx.core.view.isVisible
import at.sunilson.appointments.R
import at.sunilson.appointments.domain.entities.Appointment
import at.sunilson.appointments.domain.entities.Service
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.presentationcore.extensions.formatPattern
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
internal abstract class HistoryListItemModel :
    EpoxyModelWithHolder<HistoryListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.history_list_item

    @EpoxyAttribute
    var headline: String? = null

    @EpoxyAttribute
    lateinit var service: Service

    override fun bind(holder: Holder) = holder.run {
        title.text = service.label
        subTitle.text = "${service.date?.formatPattern("dd.MM.YYYY")}"
        header.isVisible = headline != null
        header.text = headline
    }

    class Holder : KotlinEpoxyHolder() {
        val title by bind<TextView>(R.id.title)
        val subTitle by bind<TextView>(R.id.subtitle)
        val header by bind<TextView>(R.id.section_header)
    }
}
