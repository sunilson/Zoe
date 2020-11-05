package at.sunilson.vehicle.presentation.settingsDialog

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StyleRes
import at.sunilson.presentationcore.epoxy.KotlinEpoxyHolder
import at.sunilson.vehicle.R
import at.sunilson.vehiclecore.domain.entities.Vehicle
import coil.load
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView

@EpoxyModelClass
abstract class ThemeListItemModel : EpoxyModelWithHolder<ThemeListItemModel.Holder>() {

    override fun getDefaultLayout() = R.layout.theme_list_item

    @EpoxyAttribute(hash = false)
    lateinit var onThemeChosen: (Int) -> Unit

    override fun bind(holder: Holder) = holder.run {
        red.setOnClickListener { onThemeChosen(R.style.Base_Theme_Zoe_Red) }
        blue.setOnClickListener { onThemeChosen(R.style.Base_Theme_Zoe_Blue) }
        orange.setOnClickListener { onThemeChosen(R.style.Base_Theme_Zoe_Orange) }
        green.setOnClickListener { onThemeChosen(R.style.Base_Theme_Zoe_Green) }
    }

    class Holder : KotlinEpoxyHolder() {
        val red by bind<MaterialCardView>(R.id.red)
        val blue by bind<MaterialCardView>(R.id.blue)
        val orange by bind<MaterialCardView>(R.id.orange)
        val green by bind<MaterialCardView>(R.id.green)
    }
}