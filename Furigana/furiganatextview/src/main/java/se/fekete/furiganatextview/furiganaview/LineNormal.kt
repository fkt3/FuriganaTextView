package se.fekete.furiganatextview.furiganaview

import android.graphics.Canvas
import android.graphics.Paint

import java.util.Vector

class LineNormal(val paint: Paint) {
    // Text
    private val m_text = Vector<TextNormal>()

    // Elements
    fun size(): Int {
        return m_text.size
    }

    fun add(text: Vector<TextNormal>) {
        m_text.addAll(text)
    }

    // Draw
    fun draw(canvas: Canvas, y: Float) {
        var y = y
        y -= paint.descent()
        var x = 0.0f
        for (text in m_text)
            x += text.draw(canvas, x, y)
    }
}