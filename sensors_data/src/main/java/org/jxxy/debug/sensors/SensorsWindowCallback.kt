@file:Suppress("ktlint:standard:filename")

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import org.jxxy.debug.maidian.R
import org.jxxy.debug.sensors.getTargetViews

class SensorsWindowCallback(
    private val activity: Activity,
    private val callback: Window.Callback,
) : Window.Callback {
    private val TAG = "SensorsBind"

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return callback.dispatchKeyEvent(event)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
        return callback.dispatchKeyShortcutEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val rootView = activity.window.decorView as ViewGroup
            val views = ArrayList<View>()
            rootView.getTargetViews(event, views)
            views.forEach {
                Log.d(
                    TAG,
                    "SensorsWindowCallback: " + activity.resources.getResourceName(if (it.id > 10) it.id else R.id.sensors_default_id),
                )
            }
        }
        return callback.dispatchTouchEvent(event)
    }

    override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
        return callback.dispatchTrackballEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        return callback.dispatchGenericMotionEvent(event)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        return callback.dispatchPopulateAccessibilityEvent(event)
    }

    override fun onCreatePanelView(featureId: Int): View? {
        return callback.onCreatePanelView(featureId)
    }

    override fun onCreatePanelMenu(
        featureId: Int,
        menu: Menu,
    ): Boolean {
        return callback.onCreatePanelMenu(featureId, menu)
    }

    override fun onPreparePanel(
        featureId: Int,
        view: View?,
        menu: Menu,
    ): Boolean {
        return callback.onPreparePanel(featureId, view, menu)
    }

    override fun onMenuOpened(
        featureId: Int,
        menu: Menu,
    ): Boolean {
        return callback.onMenuOpened(featureId, menu)
    }

    override fun onMenuItemSelected(
        featureId: Int,
        item: MenuItem,
    ): Boolean {
        return callback.onMenuItemSelected(featureId, item)
    }

    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
        callback.onWindowAttributesChanged(attrs)
    }

    override fun onContentChanged() {
        callback.onContentChanged()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        callback.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        callback.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        callback.onDetachedFromWindow()
    }

    override fun onPanelClosed(
        featureId: Int,
        menu: Menu,
    ) {
        callback.onPanelClosed(featureId, menu)
    }

    override fun onSearchRequested(): Boolean {
        return callback.onSearchRequested()
    }

    @TargetApi(23)
    override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
        return callback.onSearchRequested(searchEvent)
    }

    override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
        return this.callback.onWindowStartingActionMode(callback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onWindowStartingActionMode(
        callback: ActionMode.Callback,
        type: Int,
    ): ActionMode? {
        return this.callback.onWindowStartingActionMode(callback, type)
    }

    override fun onActionModeStarted(mode: ActionMode) {
        callback.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode) {
        callback.onActionModeFinished(mode)
    }
}
