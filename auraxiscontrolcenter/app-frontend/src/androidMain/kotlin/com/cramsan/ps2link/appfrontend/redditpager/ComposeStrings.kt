package com.cramsan.ps2link.appfrontend.redditpager

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.ui.R

@Composable
actual fun TwitterUploadedBy(createdTime: String, author: String): String {
    return stringResource(id = R.string.twitter_upload_by, formatArgs = arrayOf(createdTime, author))
}

@Composable
actual fun TwitterComments(count: Int): String {
    return stringResource(id = R.string.twitter_comments, formatArgs = arrayOf(count))
}