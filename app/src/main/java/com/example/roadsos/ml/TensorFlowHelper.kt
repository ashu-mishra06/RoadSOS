package com.example.roadsos.ml

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TensorflowHelper(context: Context) {

    private var interpreter: Interpreter

    init {

        val assetFile =
            context.assets.open("crash_model.tflite")

        val modelBytes =
            assetFile.readBytes()

        val buffer =
            ByteBuffer.allocateDirect(modelBytes.size)

        buffer.order(ByteOrder.nativeOrder())

        buffer.put(modelBytes)

        interpreter = Interpreter(buffer)
    }

    fun analyzeAudio(audioData: ShortArray): Float {

        try {

            val inputBuffer =
                ByteBuffer.allocateDirect(1024 * 4)

            inputBuffer.order(ByteOrder.nativeOrder())

            for (i in 0 until 1024) {

                val normalized =

                    audioData[i].toFloat() / 32767f

                inputBuffer.putFloat(normalized)
            }

            val output =
                Array(1) { FloatArray(1) }

            interpreter.run(
                inputBuffer,
                output
            )

            val score = output[0][0]

            Log.d(
                "CRASH_SCORE",
                score.toString()
            )

            return score

        } catch (e: Exception) {

            Log.e(
                "TFLITE_ERROR",
                e.message.toString()
            )

            return 0f
        }
    }
}