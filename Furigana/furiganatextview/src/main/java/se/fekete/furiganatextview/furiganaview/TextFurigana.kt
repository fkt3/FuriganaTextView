package se.fekete.furiganatextview.furiganaview

import android.graphics.Canvas
import android.graphics.Paint

class TextFurigana(private val m_text: String, private val m_paint_f: Paint) {

    // Coordinates
    private var m_offset = 0.0f
    private var m_width = 0.0f

    init {
        m_width = m_paint_f.measureText(m_text)
    }

    fun offset_get(): Float {
        return m_offset
    }

    fun offset_set(value: Float) {
        m_offset = value
    }

    fun width(): Float {
        return m_width
    }

    fun draw(canvas: Canvas, x: Float, y: Float) {
        var x = x
        x -= m_width / 2.0f
        canvas.drawText(m_text, 0, m_text.length, x, y, m_paint_f)
    }
}