package at.sunilson.vehicle.presentation.vehicleOverview

import android.graphics.Bitmap
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeAnimationCoordinator @Inject constructor() {
    var themeImage: Bitmap? = null
    var themeRecreationFinished: () -> Unit = {}
}