package com.cramsan.petproject.downloadcatalog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cramsan.petproject.R

class DownloadCatalogDialogFragment : DialogFragment() {

    private lateinit var model: DownloadCatalogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(DownloadCatalogViewModel::class.java)
        model.observableLoading().observe(this, Observer<Boolean> { isLoading ->
            if (!isLoading) {
                dismiss()
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) {
            throw IllegalStateException("Activity cannot be null")
        }

        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
            .setMessage(R.string.navigation_drawer_open)
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
