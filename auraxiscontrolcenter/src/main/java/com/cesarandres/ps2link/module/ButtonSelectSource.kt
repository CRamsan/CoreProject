package com.cesarandres.ps2link.module

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageButton
import android.widget.LinearLayout
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.dbg.DBGCensus
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace
import com.cramsan.framework.metrics.MetricsInterface

/**
 * @author Cesar Ramirez
 *
 *
 * This class will be in charge of switching between the three available buttons to select
 * from the ps2:v1, ps2ps4us:v2 and ps2ps4eu:v2 namespace
 */
@SuppressLint("CutPasteId")
class ButtonSelectSource(
    private val context: Context,
    root: ViewGroup,
    private val metrics: MetricsInterface,
    private val dbgCensus: DBGCensus
) {
    var namespace = Namespace.PS2PC

    private val pcNamespaceButton: ImageButton
    private val ps4euNamespaceButton: ImageButton
    private val ps4usNamespaceButton: ImageButton

    var listener: SourceSelectionChangedListener? = null

    init {
        val settings = context.getSharedPreferences("PREFERENCES", 0)
        val lastNamespace = settings.getString("lastNamespace", Namespace.PS2PC.name)
        val currentNamespace = Namespace.valueOf(lastNamespace!!)
        namespace = currentNamespace

        pcNamespaceButton = View.inflate(context, R.layout.layout_title_button, null) as ImageButton
        pcNamespaceButton.setImageResource(R.drawable.namespace_pc)
        pcNamespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PS4US) }

        ps4euNamespaceButton =
            View.inflate(context, R.layout.layout_title_button, null) as ImageButton
        ps4euNamespaceButton.setImageResource(R.drawable.namespace_ps4eu)
        ps4euNamespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PC) }

        ps4usNamespaceButton =
            View.inflate(context, R.layout.layout_title_button, null) as ImageButton
        ps4usNamespaceButton.setImageResource(R.drawable.namespace_ps4us)
        ps4usNamespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PS4EU) }

        val params = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        val r = context.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            r.displayMetrics
        ).toInt()

        params.setMargins(px, px, px, px)

        root.addView(pcNamespaceButton, params)
        root.addView(ps4euNamespaceButton, params)
        root.addView(ps4usNamespaceButton, params)
        updateButtonState(currentNamespace)
    }

    private fun updateButtonState(namespace: Namespace) {
        val settings = this.context.getSharedPreferences("PREFERENCES", 0)
        val editor = settings.edit()
        editor.putString("lastNamespace", namespace.name)
        editor.commit()
        this.namespace = namespace
        pcNamespaceButton.visibility = if (namespace == Namespace.PS2PC) View.VISIBLE else View.GONE
        ps4euNamespaceButton.visibility =
            if (namespace == Namespace.PS2PS4EU) View.VISIBLE else View.GONE
        ps4usNamespaceButton.visibility =
            if (namespace == Namespace.PS2PS4US) View.VISIBLE else View.GONE
        metrics.log(TAG, "OnNamespaceButtonClicked", mapOf("Namespace" to namespace.name))

        if (listener != null) {
            listener!!.onSourceSelectionChanged(namespace)
        }
    }

    fun removeButtons(context: Context, root: ViewGroup) {
        root.removeView(pcNamespaceButton)
        root.removeView(ps4euNamespaceButton)
        root.removeView(ps4usNamespaceButton)
    }

    interface SourceSelectionChangedListener {
        fun onSourceSelectionChanged(selectedNamespace: DBGCensus.Namespace)
    }

    companion object {
        private const val TAG = "ButtonSelectSource"
    }
}
