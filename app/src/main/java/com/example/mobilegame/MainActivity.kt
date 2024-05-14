package com.example.mobilegame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameLogic {
    private lateinit var basicLayout: LinearLayout
    private lateinit var startButton: Button
    private lateinit var sGameView: Gameview
    private lateinit var gScore: TextView
    private lateinit var gName: TextView
    private lateinit var gInfor: TableLayout
    private lateinit var gHscore: TextView
    private lateinit var gOver: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ManageSound: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        startButton = findViewById(R.id.start)
        basicLayout = findViewById(R.id.basicLayout)
        gScore = findViewById(R.id.score)
        gName = findViewById(R.id.Gtitle)
        gInfor = findViewById(R.id.Ginfo)
        gHscore = findViewById(R.id.hScore)
        gOver = findViewById(R.id.gameOver)
        ManageSound = SoundManager(this)
        sGameView = Gameview(this, this)

        sharedPreferences = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

        val highestScore = loadHighScore()
        gHscore.text = "HIGHEST SCORE: $highestScore"
        gScore.visibility = View.GONE
        gOver.visibility = View.GONE

        startButton.setOnClickListener {

            basicLayout.addView(sGameView)
            startButton.visibility = View.GONE
            gName.visibility = View.GONE
            gInfor.visibility = View.GONE
            gScore.visibility = View.GONE
            gHscore.visibility = View.GONE
            gOver.visibility = View.GONE

        }
    }

    override fun endGame(mScore: Int) {
        ManageSound.soundPlay(1)
        gScore.text = "SCORE : $mScore"


        val highestScore = loadHighScore()
        if (mScore > highestScore) {
            saveHighScore(mScore)
            gHscore.text = "HIGHEST SCORE: $mScore"
        }

        basicLayout.removeView(sGameView)
        startButton.visibility = View.VISIBLE
        gName.visibility = View.VISIBLE
        gInfor.visibility = View.VISIBLE
        gScore.visibility = View.VISIBLE
        gHscore.visibility = View.VISIBLE
        gOver.visibility = View.VISIBLE
        sGameView = Gameview(this, this)

    }


    private fun saveHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("high_score", score)
        editor.apply()
    }

    private fun loadHighScore(): Int {
        return sharedPreferences.getInt("high_score", 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        ManageSound.release()
    }
}