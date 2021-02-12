package xyz.eli_annis.oscilloscope.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.lang.IllegalStateException

class AudioController() {
    var sampling : Boolean = false

    private var recorder : AudioRecord? = null
    private var thread : Thread? = null
    var processor : AudioProcessor? = null

    init {
        val n = AudioRecord.getMinBufferSize(
                8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        )

        recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                n * 10
        )
    }

    fun start(){
        sampling = true

        thread = Thread(Runnable {
            val buffers : Array<ShortArray> = Array(256) { ShortArray(160) }
            var ix = 0

            recorder?.startRecording()

            try {
                while (sampling) {
                    val buffer: ShortArray = buffers[ix++ % buffers.size]
                    val n = recorder!!.read(buffer, 0, buffer.size)
                    processor?.process(buffer)
                }
            } catch (ex: Exception){
                ex.printStackTrace()
            } finally {
                stop()
            }
        })

        try {
            thread?.start()
        } catch (ex: IllegalStateException){
            ex.printStackTrace()
            sampling = false
        }
    }

    fun stop(){
        sampling = false

        try {
            recorder?.stop()
        } catch (ex: IllegalStateException){
            ex.printStackTrace()
        }

        try {
            thread?.interrupt()
        } catch (ex: InterruptedException){
            ex.printStackTrace()
        }
    }
}