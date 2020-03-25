/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.browser.toolbar.display

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import mozilla.components.browser.toolbar.R
import mozilla.components.concept.toolbar.Toolbar.Container
import mozilla.components.concept.toolbar.Toolbar.Container.NONE
import mozilla.components.concept.toolbar.Toolbar.Container.PERSONAL
import mozilla.components.concept.toolbar.Toolbar.Container.SHOPPING
import mozilla.components.concept.toolbar.Toolbar.Container.WORK

/**
 * Internal widget to display the different icons of containers.
 */
internal class ContainerIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var container: Container = NONE
        set(value) {
            if (value != field) {
                field = value
                updateIcon()
            }
        }

    private var iconPersonal: Drawable =
        requireNotNull(AppCompatResources.getDrawable(context, DEFAULT_ICON_PERSONAL))
    private var iconShopping: Drawable =
        requireNotNull(AppCompatResources.getDrawable(context, DEFAULT_ICON_SHOPPING))
    private var iconWork: Drawable =
        requireNotNull(AppCompatResources.getDrawable(context, DEFAULT_ICON_WORK))

    private fun updateIcon() {
        val update = container.toUpdate()

        isVisible = update.visible

        contentDescription = if (update.contentDescription != null) {
            context.getString(update.contentDescription)
        } else {
            null
        }

        setImageDrawable(update.drawable)

        if (update.drawable is Animatable) {
            update.drawable.start()
        }
    }

    companion object {
        val DEFAULT_ICON_PERSONAL = R.drawable.mozac_ic_fingerprint
        val DEFAULT_ICON_SHOPPING = R.drawable.mozac_ic_cart
        val DEFAULT_ICON_WORK = R.drawable.mozac_ic_briefcase
    }

    private fun Container.toUpdate(): Update = when (this) {
        NONE -> Update(null, null, false)
        PERSONAL -> Update(iconPersonal, null, true)
        SHOPPING -> Update(iconShopping, null, true)
        WORK -> Update(iconWork, null, true)
    }
}
