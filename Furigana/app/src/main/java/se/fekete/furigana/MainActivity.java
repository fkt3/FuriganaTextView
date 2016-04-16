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
        FuriganaView furiganaView = (FuriganaView) this.findViewById(R.id.furigana_view);
        String textWithRuby = "サンシャイン６０の<ruby>展望台<rt>てんぼうだい</rt></ruby>が<ruby>新<rt>あたら</rt></ruby>しくなる";

        if (furiganaView != null) {
            textWithRuby = FuriganaUtils.parseRuby(textWithRuby);
            furiganaView.updateText(textWithRuby);
        }
    }
}
