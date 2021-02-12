package xyz.eli_annis.oscilloscope

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import xyz.eli_annis.oscilloscope.audio.AudioProcessor
import xyz.eli_annis.oscilloscope.audio.SampleQueue

class WaveformView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), AudioProcessor {

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }

    private val tracePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    //https://source.android.com/devices/accessories/headset/jack-headset-spec
    private val RANGE : Float = 32767f // PCM 16-bit max value = 32767, min value = -32768
    private val DOMAIN : Float = 16000f // Nyquist frequency, 2 * sample rate (8000)
    private val SCALEMULTIPLIER = 10f; // default scale sample values

    private var w: Float = 0f;
    private var h: Float = 0f;
    private var midH : Float = 0f;
    private var step_size : Int = 1;

    private var sampleQueue : SampleQueue = SampleQueue(DOMAIN.toInt())
    private var scale : Float = 1f

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        w = width.toFloat()
        h = height.toFloat()
        midH = h / 2f
        step_size = (DOMAIN / w).toInt();
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val data = ArrayList(sampleQueue.getData())

        canvas?.apply {

            // Draw trace
            //
            var offset = 0f
            var prevY = 0f
            for(i in 0 until data.size-1 step step_size){
                val y: Float = scaleY(data[i])
                val x = offset++

                canvas.drawLine(x-1, prevY, x, y, tracePaint)
                prevY = y
            }

            // Draw vertical grid-lines
            val yStep = h/10f;
            var y = 0f
            while(y <= h){
                drawLine(0f, y, w, y, gridPaint)
                y+=yStep
            }

            // Draw horizontal grid-lines
            val xStep = w/10f;
            var x = 0f
            while(x <= w){
                drawLine(x, 0f, x, h, gridPaint)
                x+=xStep
            }

        }
    }

    override fun process(samples: ShortArray) {
        sampleQueue.pushAll(samples)
        postInvalidate()
    }

    fun setScale(scale: Float){
        this.scale = scale
        postInvalidate()
    }

    private fun scaleY(value: Short): Float {
        val scalar = scale * SCALEMULTIPLIER
        return (value * (midH / RANGE) * scalar) + midH
    }
}