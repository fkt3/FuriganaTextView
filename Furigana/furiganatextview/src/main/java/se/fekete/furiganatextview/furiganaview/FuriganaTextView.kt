/*
 * FuriganaView widget
 * Copyright (C) 2013 sh0 <sh0@yutani.ee>
 * Licensed under Creative Commons BY-SA 3.0
 */

// Package
package se.fekete.furiganatextview.furiganaview

// Imports

import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import se.fekete.furiganatextview.R
import java.util.*


class FuriganaTextView : TextView {

    // Paints
    private var m_paint_f = TextPaint()
    private var m_paint_k_norm = TextPaint()

    // Sizes
    private var m_linesize = 0.0f
    private var m_height_n = 0.0f
    private var m_height_f = 0.0f
    private var m_linemax = 0.0f

    // Spans and lines
    private val m_span = Vector<Span>()
    private val m_line_n = Vector<LineNormal>()
    private val m_line_f = Vector<LineFurigana>()

    //attributes
    private var hasRuby: Boolean = false
    private var furiganaTextColor: Int = 0

    // Constructors
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FuriganaTextView, 0, 0)
        try {
            hasRuby = typedArray.getBoolean(R.styleable.FuriganaTextView_contains_ruby_tags, false)
            furiganaTextColor = typedArray.getColor(R.styleable.FuriganaTextView_furigana_text_color, 0)
        } finally {
            typedArray.recycle()
        }

        initialize()
    }

    private fun initialize() {
        val viewText = text
        if (viewText.isNotEmpty()) {
            setFuriganaText(viewText as String, hasRuby)
        }
    }

    /**
     * The method parseRuby converts kanji enclosed in ruby tags to the
     * format which is supported by the textview {Kanji:furigana}

     * @param textWithRuby
     * The text string with Kanji enclosed in ruby tags.
     */
    private fun replaceRuby(textWithRuby: String): String {
        var parsed = textWithRuby.replace("<ruby>", "{")
        parsed = parsed.replace("<rt>", ";")
        parsed = parsed.replace("</rt>", "")

        return parsed.replace("</ruby>", "}")
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        invalidate()
    }

    fun setFuriganaText(text: String) {
        setFuriganaText(text, hasRuby = false)
    }

    fun setFuriganaText(text: String, hasRuby: Boolean) {
        super.setText(text)

        var textToDisplay = text
        if (this.hasRuby || hasRuby) {
            textToDisplay = replaceRuby(text)
        }

        text_set(paint, textToDisplay, 0, 0)
    }

    private fun text_set(tp: TextPaint, text: String, mark_s: Int, mark_e: Int) {
        var text = text
        var mark_s = mark_s
        var mark_e = mark_e

        // Text
        m_paint_k_norm = TextPaint(tp)
        m_paint_f = TextPaint(tp)
        m_paint_f.textSize = m_paint_f.textSize / 2.0f

        // Linesize
        m_height_n = m_paint_k_norm.descent() - m_paint_k_norm.ascent()
        m_height_f = m_paint_f.descent() - m_paint_f.ascent()
        m_linesize = m_height_n + m_height_f

        // Clear spans
        m_span.clear()

        // Sizes
        m_linesize = m_paint_f.fontSpacing + Math.max(m_paint_k_norm.fontSpacing, 0f)

        // Spannify text
        while (text.isNotEmpty()) {
            var idx = text.indexOf('{')
            if (idx >= 0) {
                // Prefix string
                if (idx > 0) {
                    // Spans
                    m_span.add(Span("", text.substring(0, idx), mark_s, mark_e, m_paint_k_norm, m_paint_f))

                    // Remove text
                    text = text.substring(idx)
                    mark_s -= idx
                    mark_e -= idx
                }

                // End bracket
                idx = text.indexOf('}')
                if (idx < 1) {
                    // Error
                    break
                } else if (idx == 1) {
                    // Empty bracket
                    text = text.substring(2)
                    continue
                }

                // Spans
                val split = text.substring(1, idx).split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                m_span.add(Span(if (split.size > 1) split[1] else "", split[0], mark_s, mark_e, m_paint_k_norm, m_paint_f))

                // Remove text
                text = text.substring(idx + 1)
                mark_s -= split[0].length
                mark_e -= split[0].length

            } else {
                // Single span
                m_span.add(Span("", text, mark_s, mark_e, m_paint_k_norm, m_paint_f))
                text = ""
            }
        }

        // Invalidate view
        this.invalidate()
        this.requestLayout()
    }

    // Size calculation
    override fun onMeasure(width_ms: Int, height_ms: Int) {
        // Modes
        val wmode = View.MeasureSpec.getMode(width_ms)
        val hmode = View.MeasureSpec.getMode(height_ms)

        // Dimensions
        val wold = View.MeasureSpec.getSize(width_ms)
        val hold = View.MeasureSpec.getSize(height_ms)

        if (text.isNotEmpty()) {
            // Draw mode
            if (wmode == View.MeasureSpec.EXACTLY || wmode == View.MeasureSpec.AT_MOST && wold > 0) {
                // Width limited
                text_calculate(wold.toFloat())
            } else {
                // Width unlimited
                text_calculate(-1.0f)
            }
        }

        // New height
        var hnew = Math.round(Math.ceil((m_linesize * m_line_n.size.toFloat()).toDouble())).toInt()
        var wnew = wold
        if (wmode != View.MeasureSpec.EXACTLY && m_line_n.size <= 1)
            wnew = Math.round(Math.ceil(m_linemax.toDouble())).toInt()
        if (hmode != View.MeasureSpec.UNSPECIFIED && hnew > hold)
            hnew = hnew or View.MEASURED_STATE_TOO_SMALL

        // Set result
        setMeasuredDimension(wnew, hnew)
    }

    private fun text_calculate(line_max: Float) {
        // Clear lines
        m_line_n.clear()
        m_line_f.clear()

        // Sizes
        m_linemax = 0.0f

        // Check if no limits on width
        if (line_max < 0.0) {

            // Create single normal and furigana line
            val line_n = LineNormal(m_paint_k_norm)
            val line_f = LineFurigana(m_linemax, m_paint_f)

            // Loop spans
            for (span in m_span) {
                // Text
                line_n.add(span.normal())
                line_f.add(span.furigana(m_linemax))

                // Widths update
                for (width in span.widths())
                    m_linemax += width
            }

            // Commit both lines
            m_line_n.add(line_n)
            m_line_f.add(line_f)

        } else {

            // Lines
            var line_x = 0.0f
            var line_n = LineNormal(m_paint_k_norm)
            var line_f = LineFurigana(m_linemax, m_paint_f)

            // Initial span
            var span_i = 0
            var span: Span? = if (m_span.isNotEmpty()) m_span[span_i] else null

            // Iterate
            while (span != null) {
                // Start offset
                val line_s = line_x

                // Calculate possible line size
                val widths = span.widths()
                var i = 0
                while (i < widths.size) {
                    if (line_x + widths[i] <= line_max)
                        line_x += widths[i]
                    else
                        break
                    i++
                }

                // Add span to line
                if (i >= 0 && i < widths.size) {

                    // Span does not fit entirely
                    if (i > 0) {
                        // Split half that fits
                        val normal_a = Vector<TextNormal>()
                        val normal_b = Vector<TextNormal>()
                        span.split(i, normal_a, normal_b)
                        line_n.add(normal_a)
                        span = Span(normal_b)
                    }

                    // Add new line with current spans
                    if (line_n.size() != 0) {
                        // Add
                        m_linemax = if (m_linemax > line_x) m_linemax else line_x
                        m_line_n.add(line_n)
                        m_line_f.add(line_f)

                        // Reset
                        line_n = LineNormal(m_paint_k_norm)
                        line_f = LineFurigana(m_linemax, m_paint_f)
                        line_x = 0.0f

                        // Next span
                        continue
                    }

                } else {

                    // Span fits entirely
                    line_n.add(span.normal())
                    line_f.add(span.furigana(line_s))

                }

                // Next span
                span = null
                span_i++
                if (span_i < m_span.size)
                    span = m_span[span_i]
            }

            // Last span
            if (line_n.size() != 0) {
                // Add
                m_linemax = if (m_linemax > line_x) m_linemax else line_x
                m_line_n.add(line_n)
                m_line_f.add(line_f)
            }
        }

        // Calculate furigana
        for (line in m_line_f)
            line.calculate()
    }

    // Drawing
    public override fun onDraw(canvas: Canvas) {

        m_paint_k_norm.color = currentTextColor

        if (furiganaTextColor != 0) {
            m_paint_f.color = furiganaTextColor
        } else {
            m_paint_f.color = currentTextColor
        }

        // Coordinates
        var y = m_linesize

        // Loop lines
        for (i in m_line_n.indices) {
            m_line_n[i].draw(canvas, y)
            m_line_f[i].draw(canvas, y - m_height_n)
            y += m_linesize
        }
    }
}