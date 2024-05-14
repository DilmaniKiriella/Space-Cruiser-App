package com.example.mobilegame
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.graphics.Paint
import android.view.MotionEvent

class Gameview(var c :Context,var gamelogic :GameLogic):View(c) {

    private var myPaint:Paint? = null
    private var fast = 1
    private var time = 0
    private var score = 0
    private var shipPosition = 0
    private var backgroundPosition = 0
    private val Asteroids = ArrayList<HashMap<String,Any>>()

    var screenWidth = 0
    var screenHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        screenWidth = this.measuredWidth
        screenHeight = this.measuredHeight

        canvas.drawColor(Color.BLACK)

        val backgroundDrawable = resources.getDrawable(R.drawable.bg6, null)
        backgroundDrawable.setBounds(
            0, 0, screenWidth,
            screenHeight + backgroundPosition
        )
        backgroundDrawable.draw(canvas!!)

        backgroundPosition += 5

        if (backgroundPosition >= screenHeight) {
            backgroundPosition = 0
        }

        if (time % 700 < 10 + fast) {
            val map = HashMap<String, Any>()
            map["path"] = (0..2).random()
            map["Timestart"] = time
            Asteroids.add(map)
        }

        time = time + 10 + fast
        val shipWidth = screenWidth /5
        val shipHeight = shipWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val dr1 = resources.getDrawable(R.drawable.alien,null)

        dr1.setBounds(
            shipPosition * screenWidth / 3 + screenWidth / 15 +25,
            screenHeight-2 - shipHeight,
            shipPosition * screenWidth / 3 + screenWidth /15 + shipWidth - 25,
            screenHeight - 2
        )

        dr1.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in Asteroids.indices){
            try{
                val astX = Asteroids[i]["path"] as Int * screenWidth / 3 + screenWidth / 15
                var astY = time - Asteroids[i]["Timestart"] as Int
                val dr2 = resources.getDrawable(R.drawable.asteroid,null)

                dr2.setBounds(
                    astX + 25 , astY - shipHeight, astX + shipWidth- 25 , astY
                )
                dr2.draw(canvas)
                if (Asteroids[i]["path"] as Int == shipPosition){
                    if(astY > screenHeight - 2 - shipHeight
                        && astY < screenHeight - 2){
                        gamelogic.endGame(score)
                    }
                    if (astY > screenHeight + shipHeight){

                        Asteroids.removeAt(i)
                        score++
                        fast = 1 + Math.abs(score / 10)
                        if (score > highScore){
                            highScore = score
                        }
                    }
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 48f
        canvas.drawText("Score : $score", 80f ,80f, myPaint!!)
        canvas.drawText("Speed : $fast", 380f ,80f, myPaint!!)
        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < screenWidth / 2) {
                    if (shipPosition > 0) {
                        shipPosition--
                    }
                } else {
                    if (shipPosition < 2) {
                        shipPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }

}