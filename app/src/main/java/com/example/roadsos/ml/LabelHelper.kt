package com.example.roadsos.ml

import android.content.Context

class LabelHelper(
    context: Context
) {

    val labels: List<String>

    init {

        labels =
            context.assets
                .open("labels.csv")
                .bufferedReader()
                .readLines()
                .drop(1)
                .map {

                    it.split(",")[2]
                }
    }
}