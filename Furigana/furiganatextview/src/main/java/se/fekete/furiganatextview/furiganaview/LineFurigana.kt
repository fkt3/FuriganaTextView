package se.fekete.furiganatextview.furiganaview

import android.graphics.Canvas
import android.graphics.Paint
import java.util.*

class LineFurigana(val lineMax: Float, val paint: Paint) {
    // Text
    private val m_text = Vector<TextFurigana>()
    private val m_offset = Vector<Float>()

    // Add
    fun add(text: TextFurigana?) {
        if (text != null)
            m_text.add(text)
    }

    // Calculate
    fun calculate() {
        // Check size
        if (m_text.size == 0)
            return

        val r = FloatArray(m_text.size)
        for (i in m_text.indices)
            r[i] = m_text[i].offset_get()

        // a[] - constraint matrix
        val a = Array(m_text.size + 1) { FloatArray(m_text.size) }
        for (i in a.indices)
            for (j in 0..a[0].size - 1)
                a[i][j] = 0.0f
        a[0][0] = 1.0f
        for (i in 1..a.size - 2 - 1) {
            a[i][i - 1] = -1.0f
            a[i][i] = 1.0f
        }
        a[a.size - 1][a[0].size - 1] = -1.0f

        // b[] - constraint vector
        val b = FloatArray(m_text.size + 1)
        b[0] = -r[0] + 0.5f * m_text[0].width()
        for (i in 1..b.size - 2 - 1)
            b[i] = 0.5f * (m_text[i].width() + m_text[i - 1].width()) + (r[i - 1] - r[i])
        b[b.size - 1] = -lineMax + r[r.size - 1] + 0.5f * m_text[m_text.size - 1].width()

        // Calculate constraint optimization
        val x = FloatArray(m_text.size)
        for (i in x.indices)
            x[i] = 0.0f
        val co = QuadraticOptimizer(a, b)
        co.calculate(x)
        for (i in x.indices)
            m_offset.add(x[i] + r[i])
    }

    // Draw
    fun draw(canvas: Canvas, y: Float) {
        var y = y
        y -= paint.descent()
        if (m_offset.size == m_text.size) {
            // Render with fixed offsets
            for (i in m_offset.indices)
                m_text[i].draw(canvas, m_offset[i], y)
        } else {
            // Render with original offsets
            for (text in m_text)
                text.draw(canvas, text.offset_get(), y)
        }
    }
}
