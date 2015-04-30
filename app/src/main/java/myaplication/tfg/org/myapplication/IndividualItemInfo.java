package myaplication.tfg.org.myapplication;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class IndividualItemInfo extends ActionBarActivity {
    private ImageButton imageButton;
    private ImageButton plusbutton;
    private ImageButton minusbutton;
    private TextView quantity;
    private Spinner spinner;
    private LinearLayout layoutSize;             //查看图片
    private LinearLayout layoutCart;
    private LinearLayout layoutLogin;
    private LinearLayout layoutQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCusmizedActionBar();
        getIndividualItemInfo();
        addItemSpinners();
        setQuantityButton();
        bottomMenu();
    }

    private void bottomMenu() {
        layoutSize=(LinearLayout)findViewById(R.id.layout_watch);
        layoutCart=(LinearLayout)findViewById(R.id.layout_add_cart);
        layoutLogin=(LinearLayout)findViewById(R.id.layout_login);
        layoutQuantity=(LinearLayout)findViewById(R.id.layout_quantity);

        layoutSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IndividualItemInfo.this, "You have choose Size", Toast.LENGTH_SHORT).show();
                layoutSize.setEnabled(true);
                layoutCart.setEnabled(false);
            }
        });

    }

    private void setQuantityButton() {
        plusbutton = (ImageButton)findViewById(R.id.plus);
        minusbutton = (ImageButton)findViewById(R.id.minus);
        quantity =(TextView)findViewById(R.id.quantity);
        quantity.setText("1");
        listen();

    }

    private void listen() {
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(quantity.getText().toString());
                number = number+1;
                if(number>9){
                    quantity.setText("10");
                    plusbutton.setEnabled(false);
                }
                else{
                    quantity.setText(Integer.toString(number));
                    plusbutton.setEnabled(true);
                    minusbutton.setEnabled(true);
                }
            }
        });

        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(quantity.getText().toString());
                number = number-1;
                if(number<2){
                    quantity.setText("1");
                    minusbutton.setEnabled(false);
                }
                else{
                    quantity.setText(Integer.toString(number));
                    minusbutton.setEnabled(true);
                    plusbutton.setEnabled(true);
                }
            }
        });

    }

    private void addItemSpinners() {
        String[] spinnerinfo = new String[]{"XS","S","M","L","XL"};
        List<String> spinner_list=new ArrayList<>();
        for(int i=0; i<spinnerinfo.length;i++){
            spinner_list.add(spinnerinfo[i]);
        }
        spinner = (Spinner)findViewById(R.id.detail_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_gallery_item,spinner_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

    }

    private void getIndividualItemInfo() {
        setContentView(R.layout.activity_individual_item_info);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        ProductConfigurable p =(ProductConfigurable)bundle.getSerializable("key");
        TextView title = (TextView)findViewById(R.id.detail_title);
        TextView price = (TextView)findViewById(R.id.detail_price);
        TextView description = (TextView)findViewById(R.id.detail_description);
        TextView stock =(TextView)findViewById(R.id.stock);
        ImageView image = (ImageView)findViewById(R.id.detail_image);
        String fullprice = "Price: "+p.getPrice();
        title.setText(p.getTitle());
        if(p.getTitle().equals("VOLCOM SUPERNATURAL INS JACKET ELECTRIC GREEN - 2015")){
            stock.setText("In Stock");
            stock.setTextColor(Color.parseColor("#31B404"));
        }
        else{
            stock.setText("Out Of Stock");
            stock.setTextColor(Color.parseColor("#DF0101"));
        }
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
