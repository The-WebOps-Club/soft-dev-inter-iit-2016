package example.com.alerto;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlertActivity extends AppCompatActivity {
    TextView titletext, texttext;
    Button mapbutton, acceptbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titletext = (TextView) findViewById(R.id.titleText);
        texttext = (TextView) findViewById(R.id.textText);
        acceptbutton = (Button) findViewById(R.id.acceptbutton);
        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("title")!= null)
        {
            titletext.setText(bundle.getString("title"));
        }

        if(bundle.getString("text")!= null)
        {
            texttext.setText(bundle.getString("text"));
        }
    }

}
