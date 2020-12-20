package com.cesarandres.ps2link.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BoldButton(text: String) {
    Text("A day in Shark Fin Cove")
    Text("Davenport, California")
    Text("December 2018")
}

@Preview
@Composable
fun DefaultPreview() {
    BoldButton("Demo content")
}
