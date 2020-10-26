package at.sunilson.vehicleDetails.presentation.epoxy.models

import android.widget.ImageView
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicleDetails.R
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@EpoxyModelClass
abstract class DetailListImageModel : EpoxyModelWithHolder<DetailListImageModel.Holder>(),
    CoroutineScope {

    override val coroutineContext = Dispatchers.Main + SupervisorJob()

    override fun getDefaultLayout() = R.layout.vehicle_details_list_image

    private var imageJob: Job? = null

    @EpoxyAttribute
    lateinit var transitionName: String

    @EpoxyAttribute
    lateinit var imageUrl: String

    @EpoxyAttribute
    lateinit var imageLoaded: () -> Unit

    override fun bind(holder: Holder) {
        if (imageJob?.isActive == true) return
        holder.image.transitionName = transitionName
        imageJob = launch {
            holder.image.load(imageUrl).await()
            imageLoaded()
        }
    }

    override fun unbind(holder: Holder) {
        super.unbind(holder)
        imageJob?.cancel()
        imageJob = null
    }

    class Holder : KotlinEpoxyHolder() {
        val image by bind<ImageView>(R.id.vehicle_image)
    }
}
