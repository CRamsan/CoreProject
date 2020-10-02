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

    private val namespaceButton: ImageButton

    var listener: SourceSelectionChangedListener? = null

    init {
        val settings = context.getSharedPreferences("PREFERENCES", 0)
        val lastNamespace = settings.getString("lastNamespace", Namespace.PS2PC.name)
        val currentNamespace = Namespace.valueOf(lastNamespace!!)
        namespace = currentNamespace

        namespaceButton = View.inflate(context, R.layout.layout_title_button, null) as ImageButton

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

        root.addView(namespaceButton, params)
        updateButtonState(currentNamespace)
    }

    private fun updateButtonState(namespace: Namespace) {
        val settings = this.context.getSharedPreferences("PREFERENCES", 0)
        val editor = settings.edit()
        editor.putString("lastNamespace", namespace.name)
        editor.apply()
        this.namespace = namespace

        when (namespace) {
            Namespace.PS2PC -> {
                namespaceButton.setImageResource(R.drawable.namespace_pc)
                namespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PS4US) }
            }
            Namespace.PS2PS4US -> {
                namespaceButton.setImageResource(R.drawable.namespace_ps4us)
                namespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PS4EU) }
            }
            Namespace.PS2PS4EU -> {
                namespaceButton.setImageResource(R.drawable.namespace_ps4eu)
                namespaceButton.setOnClickListener { updateButtonState(Namespace.PS2PC) }
            }
        }
        namespaceButton.visibility = View.VISIBLE
        metrics.log(TAG, "OnNamespaceButtonClicked", mapOf("Namespace" to namespace.name))

        if (listener != null) {
            listener!!.onSourceSelectionChanged(namespace)
        }
    }

    fun removeButtons(context: Context, root: ViewGroup) {
        root.removeView(namespaceButton)
    }

    interface SourceSelectionChangedListener {
        fun onSourceSelectionChanged(selectedNamespace: DBGCensus.Namespace)
    }

    companion object {
        const val TAG = "ButtonSelectSource"
    }
}
