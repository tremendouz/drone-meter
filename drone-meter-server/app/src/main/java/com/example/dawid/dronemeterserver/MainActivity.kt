package com.example.dawid.dronemeterserver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.PermissionChecker
import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import org.jetbrains.anko.toast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info



class MainActivity : AppCompatActivity(), AnkoLogger {

    val REQUEST_RECORD_AUDIO_PERMISSION = 0

    val SAMPLE_RATE: Int = 44100
    val minBufferSize by lazy {
        AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)
    }
    var isRecording: Boolean = true
    lateinit var audioRecord: AudioRecord

    lateinit var file: File

    lateinit var fos: FileOutputStream


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBasicAudioPerm()
        requestEnableAudioRecord()
        recordAudio()

    }

    fun checkBasicAudioPerm() {
        val perm = PermissionChecker.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (perm != PermissionChecker.PERMISSION_GRANTED){
            finish()
            System.exit(0)
        }
    }

    fun requestEnableAudioRecord() = requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_RECORD_AUDIO_PERMISSION)

    fun checkIfCorrectBufferSize() = minBufferSize != AudioTrack.ERROR ||
            minBufferSize != AudioTrack.ERROR_BAD_VALUE


    fun recordAudio() {
        Thread().run {
            var audioBuffer = ByteArray(minBufferSize)
            val genFileName = generateFileName()

            file = File(filesDir, genFileName)
            toast("Saving audio in $filesDir")
            fos = FileOutputStream(file)

            audioRecord = AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, minBufferSize)

            if (checkIfCorrectBufferSize()) toast("Correct Buffer Size")


            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) toast("Audio Record Not Initialized")
            else audioRecord.startRecording()
            toast("Recording to file $genFileName")
            info("File name is $genFileName and the path is $filesDir")


            while (isRecording) {
                var read = audioRecord.read(audioBuffer, 0, audioBuffer.size)
                fos.write(audioBuffer, 0, read)
                isRecording = false

            }
            audioRecord.stop()
            audioRecord.release()

            if(file.exists()) toast("Recording was successful")

        }
    }

    fun generateFileName() = (System.currentTimeMillis() / 1000).toString() + ".pcm"

}
