package se.fekete.furiganatextview.furiganaview

import android.graphics.Paint
import java.util.*

internal class Span {
    // Text
    private var m_furigana: TextFurigana? = null
    private var m_normal = Vector<TextNormal>()

    // Widths
    private val m_width_chars = Vector<Float>()
    private var m_width_total = 0.0f

    // Constructors
    constructor(text_f: String, text_k: String, mark_s: Int, mark_e: Int, paint: Paint, paint_f: Paint) {

        var mark_s = mark_s
        var mark_e = mark_e

        // Furigana text
        if (text_f.length > 0)
            m_furigana = TextFurigana(text_f, paint_f)

        // Normal text
        if (mark_s < text_k.length && mark_e > 0 && mark_s < mark_e) {

            // Fix marked bounds
            mark_s = Math.max(0, mark_s)
            mark_e = Math.min(text_k.length, mark_e)

            // Prefix
            if (mark_s > 0)
                m_normal.add(TextNormal(text_k.substring(0, mark_s), paint))

            // Marked
            if (mark_e > mark_s)
                m_normal.add(TextNormal(text_k.substring(mark_s, mark_e), paint))

            // Postfix
            if (mark_e < text_k.length)
                m_normal.add(TextNormal(text_k.substring(mark_e), paint))

        } else {

            // Non marked
            m_normal.add(TextNormal(text_k, paint))

        }

        // Widths
        widths_calculate()
    }

    constructor(normal: Vector<TextNormal>) {
        // Only normal text
        m_normal = normal

        // Widths
        widths_calculate()
    }

    // Text
    fun furigana(x: Float): TextFurigana? {
        if (m_furigana == null)
            return null
        m_furigana!!.offset_set(x + m_width_total / 2.0f)
        return m_furigana
    }

    fun normal(): Vector<TextNormal> {
        return m_normal
    }

    // Widths
    fun widths(): Vector<Float> {
        return m_width_chars
    }

    private fun widths_calculate() {
        // Chars
        if (m_furigana == null) {
            for (normal in m_normal)
                for (v in normal.width_chars())
                    m_width_chars.add(v)
        } else {
            var sum = 0.0f
            for (normal in m_normal)
                for (v in normal.width_chars())
                    sum += v
            m_width_chars.add(sum)
        }

        // Total
        m_width_total = 0.0f
        for (v in m_width_chars)
            m_width_total += v
    }

    // Split
    fun split(offset: Int, normal_a: Vector<TextNormal>, normal_b: Vector<TextNormal>) {
        var offset = offset
        // Check if no furigana
        assert(m_furigana == null)

        // Split normal list
        for (cur in m_normal) {
            if (offset <= 0) {
                normal_b.add(cur)
            } else if (offset >= cur.length()) {
                normal_a.add(cur)
            } else {
                val split = cur.split(offset)
                normal_a.add(split[0])
                normal_b.add(split[1])
            }
            offset -= cur.length()
        }
    }
}