package myaplication.tfg.org.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MasVendidos extends ActionBarActivity {
    private List<ProductConfigurable> productConfigurables;
    private MyProductAdapter adapter1;
    private SoapSerializationEnvelope env;
    private SoapObject request;
    private String sessionId;
    private HttpTransportSE androidHttpTransport;
    private List<String> size;
    private List<String> product_id;
    private ProductConfigurable p_configurable;
    private ProductSimple pp_simple;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_vendidos);
        size = new ArrayList<>();
        productConfigurables = new ArrayList<>();
        product_id = new ArrayList<>();
        pp_simple =new ProductSimple();
        new DownLoad().execute();
    }


       private class DownLoad extends AsyncTask<String, Float, String> {
           ProgressDialog pDialog;
           SoapObject r;
           String NAMESPACE = "urn:Magento";
           String URL = "http://mininegocio.es/api/v2_soap/";

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               Log.d("Hi", "Download Commencing");
               pDialog = new ProgressDialog(MasVendidos.this);
               String message = "";
               SpannableString ss2 = new SpannableString(message);
               ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
               ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
               pDialog.setMessage(ss2);
               pDialog.setCancelable(false);
               pDialog.show();
           }

           @Override
           protected String doInBackground(String... params) {
               try {
                   setupSessionLogin();
 /*               StringArraySerializer stringArray = new StringArraySerializer();
                  stringArray.add("7");
                  stringArray.add("8");
                  PropertyInfo stringArrayProperty = new PropertyInfo();
                  stringArrayProperty.setName("products");
                  stringArrayProperty.setValue(stringArray);
                  stringArrayProperty.setType(stringArray.getClass());
*/
                   getAllListItem();
                   getSizeLabel();

               } catch (Exception e) {
                   e.printStackTrace();
               }
               return "Executed!";

           }


           private void setupSessionLogin() throws IOException, XmlPullParserException {
               env = new SoapSerializationEnvelope(
                       SoapEnvelope.VER11);
               env.dotNet = false;
               env.xsd = SoapSerializationEnvelope.XSD;
               env.enc = SoapSerializationEnvelope.ENC;
               request = new SoapObject(NAMESPACE, "login");
               request.addProperty("username", "jin");
               request.addProperty("apiKey", "1234567890");
               env.setOutputSoapObject(request);
               androidHttpTransport = new HttpTransportSE(URL);
               androidHttpTransport.call("", env);
               Object result = env.getResponse();
               sessionId = result.toString();
           }

           private void getAllListItem() throws IOException, XmlPullParserException {
               request = new SoapObject(NAMESPACE, "catalogProductList");
               request.addProperty("sessionId", sessionId);
               env.setOutputSoapObject(request);
               androidHttpTransport.call("", env);
               r = (SoapObject) env.getResponse();
               for (int i = 0; i < r.getPropertyCount(); i++) {
                   SoapObject child = (SoapObject) r.getProperty(i);
                   product_id.add((String) child.getProperty("product_id"));
               }
               getIndividualProductInfo();
           }

           public void getIndividualProductInfo() throws IOException, XmlPullParserException {
                String temp =new String();
                ProductConfigurable p = new ProductConfigurable();
               for (String id : product_id) {
                   request = new SoapObject(NAMESPACE, "catalogProductInfo");
                   request.addProperty("sessionId", sessionId);
                   request.addProperty("productId", id);
                   env.setOutputSoapObject(request);
                   androidHttpTransport.call("", env);
                   r = (SoapObject) env.getResponse();
                   String name = r.getProperty("name").toString();
                   String type = r.getProperty("type").toString();
                   if(!name.equals(temp) && type.equals("configurable")){
                       createConfigurableProduct(r);
                       temp = name;
                   //    productConfigurables.add(p_configurable);
                   }
                   else{
                       createSimpleProduct(r);
                        Log.d("`product simple",pp_simple.getTitle());
                      // p_configurable.addNewSimpleProduct(pp_simple);
                   }
                   Log.d("productInformation", r.toString());
               }
           }

           private void createSimpleProduct(SoapObject r) {


                    pp_simple.setProduct_id((String)r.getProperty("product_id"));
                    pp_simple.setSku((String)r.getProperty("sku"));
                    pp_simple.setTitle((String)r.getProperty("name"));
                    pp_simple.setDescription((String)r.getProperty("description"));
                    pp_simple.setPrice((String)r.getProperty("price"));
           }

           private void createConfigurableProduct(SoapObject r) {

           }

           private void getSizeLabel() throws IOException, XmlPullParserException {
               request = new SoapObject(NAMESPACE, "catalogProductAttributeOptions");
               request.addProperty("sessionId", sessionId);
               request.addProperty("attributeId", "size");
               env.setOutputSoapObject(request);
               androidHttpTransport.call("", env);
               r = (SoapObject) env.getResponse();

               for (int i = 1; i < r.getPropertyCount(); i++) {
                   SoapObject result = (SoapObject) r.getProperty(i);
                   String sizeInfo = (String) result.getProperty("label");
                   size.add(sizeInfo);
               }
           }

           protected void onProgressUpdate(Float... valores) {
               int p = Math.round(100 * valores[0]);
               pDialog.setProgress(p);
           }

           @Override
           protected void onPostExecute(String result) {
               super.onPostExecute(result);
               Log.d("Hi", "Done Downloading.");

               pDialog.dismiss();

           }


       }

    private void createItemList(){
        ListView oferta = (ListView)findViewById(R.id.top_seller_list);
        productConfigurables = new ArrayList<ProductConfigurable>();
        String[] name = new String[]{"VOLCOM SUPERNATURAL INS JACKET ELECTRIC GREEN - 2015", "BURTON WOMAN SOCIETY PANT BLUE-RAY NOVEAU NEON - 2014","BURTON WB PELE MITT KAMANA WANNA LEI YA - 2015","SALOMON ASSASSIN 155 - 2015"};
        String[] price = new String[]{"256.5€", "120.95€","34.97€","84.00€"};
        String[] description = new String[]{"Great Jacket", "Comfortable snowPants","LowPrice, good quality gloves","New model of SnowBoard with incredible price"};
        int[] images = new int[]{R.drawable.jacket,R.drawable.pant2,R.drawable.gloves,R.drawable.board};

        for(int i =0;i<name.length;i++) {
            ProductConfigurable p = new ProductConfigurable();
            p.setTitle(name[i]);
            p.setImage(images[i]);
            p.setPrice(price[i]);
            p.setDescription(description[i]);
            productConfigurables.add(p);
        }
        adapter1 = new MyProductAdapter(this, productConfigurables,R.layout.topseller_list);
        oferta.setAdapter(adapter1);
        oferta.setOnItemClickListener(new productDetailClickListener());
    }


    private class productDetailClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            loadProductDetail(position);
        }
    }

    private void loadProductDetail(int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", productConfigurables.get(position));
        Intent intent = new Intent(this,IndividualItemInfo.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.customactionbar, null);
        ImageButton imageButton = (ImageButton) findViewById(R.id.shopCartButton);
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
        return true;
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
}
