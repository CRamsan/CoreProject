package com.cramsan.awsgame.subsystems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import java.util.*

/**
 * Class that provides a simple API to handle sound and music
 */
class AudioManager : IGameSubsystem {
    enum class MUSIC {
        BG_1
    }

    enum class SOUND {
        ATTACK, BELL
    }

    private var musicMap: HashMap<MUSIC, Music>? = null
    private var soundMap: HashMap<SOUND, Sound>? = null
    override fun OnGameLoad() {}
    /**
     * Call this function when loading a scene.
     * This will load the required sounds as well as prepare the music playlist
     */
    override fun OnScreenLoad() {
        musicMap = HashMap()
        soundMap = HashMap()
        for (enumSound in SOUND.values()) {
            val filePath: String? = when (enumSound) {
                SOUND.ATTACK -> "knife-slash.ogg"
                SOUND.BELL -> "bell.wav"
            }
            val sound = Gdx.audio.newSound(Gdx.files.internal(filePath))
            soundMap!![enumSound] = sound
        }
        for (enumMusic in MUSIC.values()) {
            val filePath = when (enumMusic) {
                MUSIC.BG_1 -> "bg_music.wav"
            }
            val music = Gdx.audio.newMusic(Gdx.files.internal(filePath))
            musicMap!![enumMusic] = music
        }
    }

    /**
     * Call this function to unload any assets for the current scene
     */
    override fun OnScreenClose() {
        for (sound in soundMap!!.values) {
            sound.dispose()
        }
        for (music in musicMap!!.values) {
            music.dispose()
        }
        soundMap!!.clear()
        musicMap!!.clear()
        soundMap = null
        musicMap = null
    }

    override fun OnGameClose() {}
    fun PlaySound(sound: SOUND?) {
        soundMap!![sound]!!.play()
    }

    fun PlayMusic() {
        musicMap!![MUSIC.BG_1]!!.play()
    }
}