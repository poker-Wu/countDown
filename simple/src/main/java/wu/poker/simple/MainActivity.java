package wu.poker.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import wu.poker.countdownview.CountDownView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CountDownView view = (CountDownView) findViewById(R.id.cdView);
        view.setListener(new CountDownView.CompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(getApplicationContext(),"count down complete~~",Toast.LENGTH_LONG);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startCount();

            }
        });
    }
}
