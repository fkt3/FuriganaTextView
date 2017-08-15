# FuriganaTextView
Custom TextView for Android for rendering Japanese text with furigana.
[Licensed under Creative Commons BY-SA 3.0](http://creativecommons.org/licenses/by-sa/3.0/)

# Introduction
FuriganaTextView is a Textview for Android that supports rendering of furigana characters above Japanese kanji.
The FuriganaTextView builds upon the furigana-view written by [sh0](https://github.com/sh0/furigana-view). The TextView currently supports two xml attributes `app:contains_ruby_tags"` which is a boolean value and tells the FuriganaTextView that the text which is set contains `<ruby>` tags. The second attribute `app:furigana_text_color` takes a color and can be used to color the furigana separately from the main text. 

# Examples

##### Using FuriganaTextView in a Xml layout file.

 ```
    <se.fekete.furiganatextview.furiganaview.FuriganaTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="サンシャイン６０の&lt;ruby&gt;展望台&lt;rt&gt;てんぼうだい&lt;/rt&gt;&lt;/ruby&gt;が&lt;ruby&gt;新&lt;rt&gt;あたら&lt;/rt&gt;&lt;/ruby&gt;しくなる"
        android:textAppearance="@style/TextAppearance.AppCompat.Title"
        android:textColor="@color/colorAccent"
        app:contains_ruby_tags="true"
        app:furigana_text_color="@color/colorPrimary" />
```

##### Using FuriganaTextView in a Kotlin or Java file.
```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val furiganaTextView = findViewById(R.id.text_view_furigana) as FuriganaTextView?
        furiganaTextView!!.setFuriganaText("サンシャイン６０の<ruby>展望台<rt>てんぼうだい</rt></ruby>が<ruby>新<rt>あたら</rt></ruby>しくなる")
    }
}
```