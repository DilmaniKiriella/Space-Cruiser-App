package com.example.mobilegame

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build


class SoundManager (context: Context) {
    private var soundList: SoundPool? = null
    private var mapSound: MutableMap<Int, Int> = mutableMapOf()

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            soundList = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build()
        } else {
            soundList = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        }
        mapSound[1] = soundList?.load(context, R.raw.dead, 1) ?: 0

    }

    fun soundPlay(soundId: Int) {
        val sound = mapSound[soundId]
        sound?.let {
            soundList?.play(it, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }
    fun release() {
        soundList?.release()
        soundList = null
    }
}
