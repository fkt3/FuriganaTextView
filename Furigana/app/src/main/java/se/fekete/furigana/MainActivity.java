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

        FuriganaView furiganaView = (FuriganaView) this.findViewById(R.id.furigana_view);
        String textWithRuby = "サンシャイン６０の<ruby>展望台<rt>てんぼうだい</rt></ruby>が<ruby>新<rt>あたら</rt></ruby>しくなる";
        textWithRuby = FuriganaUtils.parseRuby(textWithRuby); //Convert text with <ruby> tags
        furiganaView.updateText(textWithRuby); //set the text
    }
}