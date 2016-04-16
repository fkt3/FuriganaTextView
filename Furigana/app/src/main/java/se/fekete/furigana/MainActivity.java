package se.fekete.furigana;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.fekete.furiganatextview.FuriganaView;
import se.fekete.furiganatextview.utils.FuriganaUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = new TextView(this);
        textView.setTextSize(20);

        FuriganaView furiganaView = (FuriganaView) this.findViewById(R.id.furigana_view);
        String text = "は{寒気}を{防;ふせ}ぐために{厚;あつ}いコートを{着;き}ていた。";
        String textWithRuby = "は<ruby>寒気<rt>さむけ</rt></ruby>を";

        if (furiganaView != null) {
            textWithRuby = FuriganaUtils.parseRuby(textWithRuby);
            furiganaView.updateText(text);
        }
    }
}
