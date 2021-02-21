package at.sunilson.presentationcore.extensions

import androidx.navigation.NavOptions
import at.sunilson.presentationcore.R

fun NavOptions.Builder.withDefaultAnimations() = NavOptions
    .Builder()
    .setEnterAnim(R.anim.move_in_from_right)
    .setExitAnim(R.anim.move_out_to_left_slightly)
    .setPopEnterAnim(R.anim.move_in_from_left_slightly)
    .setPopExitAnim(R.anim.move_out_to_right)
    .build()
