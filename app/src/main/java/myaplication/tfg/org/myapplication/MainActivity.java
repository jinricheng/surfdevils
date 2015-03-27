package myaplication.tfg.org.myapplication;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    TextView txtView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
       // ScaleAnimation scale = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f);
        txtView = (TextView)findViewById(R.id.titul);
        txtView.startAnimation(fadeIn);
       /* scale.setDuration(1500);
        scale.setStartOffset(0);
        scale.setFillAfter(true);*/
        fadeIn.setDuration(1300);
        fadeIn.setFillAfter(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
