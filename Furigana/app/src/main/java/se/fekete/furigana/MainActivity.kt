package se.fekete.furigana

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import se.fekete.furiganatextview.furiganaview.FuriganaTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val furiganaTextView = findViewById(R.id.text_view_furigana) as FuriganaTextView?
        furiganaTextView!!.setFuriganaText("サンシャイン６０の<ruby>展望台<rt>てんぼうだい</rt></ruby>が<ruby>新<rt>あたら</rt></ruby>しくなる")
    }
}