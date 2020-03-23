package com.cramsan.awsgame.subsystems

interface IGameSubsystem {
    fun OnGameLoad()
    fun OnScreenLoad()
    fun OnScreenClose()
    fun OnGameClose()
}
