package myaplication.tfg.org.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class IndividualItemInfo extends ActionBarActivity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCusmizedActionBar();
        getIndividualItemInfo();
    }

    private void getIndividualItemInfo() {
        setContentView(R.layout.activity_individual_item_info);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        Product p =(Product)bundle.getSerializable("key");
        TextView title = (TextView)findViewById(R.id.detail_title);
        TextView price = (TextView)findViewById(R.id.detail_price);
        TextView description = (TextView)findViewById(R.id.detail_description);
        ImageView image = (ImageView)findViewById(R.id.detail_image);
        String fullprice = "Price: "+p.getPrice();
        title.setText(p.getTitle());
        image.setImageResource(p.getImage());
        description.setText(p.getDescription());
        price.setText(fullprice);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void initCusmizedActionBar() {
        getCustomizedActionBar();

    }

    public void getCustomizedActionBar(){
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.customactionbar, null);
        imageButton = (ImageButton)findViewById(R.id.shopCartButton);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

}
