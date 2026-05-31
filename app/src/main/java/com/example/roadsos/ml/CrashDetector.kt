package com.example.roadsos.ml

import android.util.Log

class CrashDetector {

    private val dangerKeywords = listOf(

        "Explosion",
        "Glass",
        "Crash",
        "Bang",
        "Boom",
        "Smash",
        "Breaking",
        "Thump",
        "Thud",
        "Shatter",
        "Clatter"
    )

    fun analyzePredictions(
        scores: FloatArray,
        labels: List<String>
    ): Boolean {

        var dangerScore = 0f

        for (i in scores.indices) {

            val label = labels[i]

            val score = scores[i]

            if (

                dangerKeywords.any {

                    label.contains(
                        it,
                        ignoreCase = true
                    )
                }

            ) {

                dangerScore += score

                Log.d(
                    "DANGER_SOUND",
                    "$label -> $score"
                )
            }
        }

        Log.d(
            "CRASH_SCORE",
            dangerScore.toString()
        )

        return dangerScore > 0.60f
    }
}