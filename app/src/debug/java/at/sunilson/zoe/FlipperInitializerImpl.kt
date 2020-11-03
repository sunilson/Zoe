package at.sunilson.zoe

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader

internal class FlipperInitializerImpl(
    private val context: Context,
    private val networkFlipperPlugin: NetworkFlipperPlugin
) : FlipperInitializer {
    override fun initFlipper() {
        SoLoader.init(context, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(context)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(context)
            client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            client.addPlugin(SharedPreferencesFlipperPlugin(context, "ZOE_APP"))
            client.addPlugin(DatabasesFlipperPlugin(context))
            client.addPlugin(networkFlipperPlugin)
            client.start()
        }
    }
}