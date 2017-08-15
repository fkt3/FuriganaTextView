package se.fekete.furiganatextview.furiganaview

import android.graphics.Canvas
import android.graphics.Paint

class TextNormal(private val m_text: String, private val m_paint: Paint) {

    private var m_width_total: Float = 0.toFloat()
    private val m_width_chars: FloatArray

    init {
        m_width_chars = FloatArray(m_text.length)
        m_paint.getTextWidths(m_text, m_width_chars)

        // Total width
        m_width_total = 0.0f
        for (v in m_width_chars)
            m_width_total += v
    }

    // Info
    fun length(): Int {
        return m_text.length
    }

    // Widths
    fun width_chars(): FloatArray {
        return m_width_chars
    }

    // Split
    fun split(offset: Int): Array<TextNormal> {
        return arrayOf(TextNormal(m_text.substring(0, offset), m_paint), TextNormal(m_text.substring(offset), m_paint))
    }

    // Draw
    fun draw(canvas: Canvas, x: Float, y: Float): Float {
        canvas.drawText(m_text, 0, m_text.length, x, y, m_paint)
        return m_width_total
    }
}