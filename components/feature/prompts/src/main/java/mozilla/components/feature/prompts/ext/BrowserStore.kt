/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.feature.prompts.ext

import mozilla.components.browser.state.action.ContentAction
import mozilla.components.browser.state.selector.findTabOrCustomTabOrSelectedTab
import mozilla.components.browser.state.selector.selectedTab
import mozilla.components.browser.state.state.ContentState
import mozilla.components.browser.state.store.BrowserStore
import mozilla.components.concept.engine.prompt.PromptRequest
import mozilla.components.feature.prompts.dialog.PromptDialogFragment
import java.lang.ref.WeakReference

/**
 * Removes the [PromptRequest] of the [ContentState] and clears the active [PromptDialogFragment]
 * with the given [sessionId] or the the selected tab if it is not specified.
 *
 * @param sessionId The id of the session which has an active prompt.
 * @param activePrompt The active [PromptDialogFragment] that is shown.
 * @param consume Callback invoked before the [PromptRequest] is removed.
 */
internal fun BrowserStore.consumePromptFrom(
    sessionId: String? = null,
    activePrompt: WeakReference<PromptDialogFragment>? = null,
    consume: (PromptRequest) -> Unit
) {
    if (sessionId == null) {
        state.selectedTab
    } else {
        state.findTabOrCustomTabOrSelectedTab(sessionId)
    }?.let { tab ->
        activePrompt?.clear()
        tab.content.promptRequest?.let {
            consume(it)
            dispatch(ContentAction.ConsumePromptRequestAction(tab.id))
        }
    }
}
