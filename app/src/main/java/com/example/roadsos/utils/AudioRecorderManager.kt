package com.example.roadsos.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log

class AudioRecorderManager {

    private var audioRecord: AudioRecord? = null

    private val sampleRate = 16000

    private val bufferSize =
        AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

    fun startRecording(
        onAudioData: (ShortArray) -> Unit
    ) {

        try {

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            if (
                audioRecord?.state !=
                AudioRecord.STATE_INITIALIZED
            ) {

                Log.e(
                    "AUDIO_ERROR",
                    "AudioRecord initialization failed"
                )

                return
            }

            audioRecord?.startRecording()

            Thread {

                val buffer = ShortArray(1024)

                while (
                    audioRecord != null &&
                    audioRecord?.recordingState ==
                    AudioRecord.RECORDSTATE_RECORDING
                ) {

                    val readSize =
                        audioRecord?.read(
                            buffer,
                            0,
                            buffer.size
                        ) ?: 0

                    if (readSize > 0) {

                        val audioChunk =
                            buffer.copyOf(readSize)

                        onAudioData(audioChunk)

                        Log.d(
                            "AUDIO_MONITOR",
                            "Audio chunk received"
                        )
                    }
                }

            }.start()

        } catch (e: Exception) {

            Log.e(
                "AUDIO_ERROR",
                e.message.toString()
            )
        }
    }

    fun stopRecording() {

        try {

            audioRecord?.stop()

            audioRecord?.release()

            audioRecord = null

        } catch (e: Exception) {

            Log.e(
                "AUDIO_ERROR",
                e.message.toString()
            )
        }
    }
}