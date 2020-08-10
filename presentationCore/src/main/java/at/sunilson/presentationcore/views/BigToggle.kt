package at.sunilson.presentationcore.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import at.sunilson.presentationcore.R
import at.sunilson.presentationcore.databinding.BigToggleBinding
import at.sunilson.presentationcore.extensions.applyStyledAttributes

class BigToggle @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private var binding = BigToggleBinding.inflate(LayoutInflater.from(context), this, true)

    var textLeft: String = ""
        set(value) {
            field = value
            binding.toggleTextLeft.text = value
        }

    var textRight: String = ""
        set(value) {
            field = value
            binding.toggleTextRight.text = value
        }

    var onToggledListener: (Boolean) -> Unit = {}

    private var initialized: Boolean = false

    var toggled: Boolean = false
        set(value) {
            if (value) {
                if (initialized) {
                    binding.toggleMotionLayout.transitionToEnd()
                } else {
                    binding.toggleMotionLayout.progress = 1f
                }
            } else {
                if (initialized) {
                    binding.toggleMotionLayout.transitionToStart()
                } else {
                    binding.toggleMotionLayout.progress = 0f
                }
            }
            field = value
            initialized = true
        }

    init {
        applyStyledAttributes(attributeSet, R.styleable.BigToggle) {
            textLeft = getString(R.styleable.BigToggle_textLeft) ?: ""
            textRight = getString(R.styleable.BigToggle_textRight) ?: ""
        }

        setOnClickListener {
            toggled = !toggled
            onToggledListener(toggled)
        }
    }
}
