package android.support.design.bottomappbar

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet

class HideableBottomAppBar : BottomAppBar {
    private val compatBehavior by lazyFast { CompatBehavior() }

    fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
        operation()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) // respect internal style
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getBehavior(): CoordinatorLayout.Behavior<BottomAppBar> {
        return compatBehavior
    }

    private inner class CompatBehavior : BottomAppBar.Behavior() {
        fun show() {
            slideUp(this@HideableBottomAppBar)
        }

        fun hide() {
            slideDown(this@HideableBottomAppBar)
        }
    }

    fun show() {
        compatBehavior.show()
    }

    fun hide() {
        compatBehavior.hide()
    }
}