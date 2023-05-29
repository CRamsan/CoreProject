package com.cramsan.framework.sample.jbcompose.mpplib

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.minesweepers.common.MainView
import com.cramsan.minesweepers.common.game.Game
import com.cramsan.minesweepers.common.game.Status

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val game = Game()
        game.configure(
            columns = 10,
            rows = 15,
            mines = 20,
        )
        game.loadAssetsAsync()

        setContent {
            val map by game.gameStateHolder.map.collectAsState()
            val time by game.gameStateHolder.time.collectAsState()
            val minesRemaining by game.gameStateHolder.minesRemaining.collectAsState()
            val gameState by game.gameStateHolder.status.collectAsState()
            val isGameReady by game.initialized.collectAsState()

            if (isGameReady) {
                MainView(
                    time,
                    minesRemaining,
                    map,
                    gameState,
                    { column, row -> game.primaryAction(column, row) },
                    { column, row -> game.secondaryAction(column, row) },
                    { game.configure() },
                )
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MainView(
        10,
        12,
        listOf(listOf()),
        Status.NORMAL,
        { _, _ -> },
        { _, _ -> },
        { },
    )
}