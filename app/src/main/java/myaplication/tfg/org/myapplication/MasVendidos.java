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
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<String,String>allSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_vendidos);
        getActivityTitle();
        initializeVariable();
        new DownLoad().execute();
    }

    /*initialize all variable that we need for this activity*/
    private void initializeVariable() {
        size = new ArrayList<>();
        productConfigurables = new ArrayList<>();
        product_id = new ArrayList<>();
        pp_simple =new ProductSimple();
        allSize = new HashMap<>();
    }

    /*initialize the activity title*/
    private void getActivityTitle() {
        Bundle bundle = getIntent().getExtras();
        String title = (String)bundle.get("name");
        TextView text= (TextView)findViewById(R.id.special_title);
        text.setText(title);
    }

  /*async task that do the work of downloading products' info*/
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
                   getAllSize();
                 //  getSizeLabel();


               } catch (Exception e) {
                   e.printStackTrace();
               }
               return "Executed!";

           }

      /*get the session id from the web */
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
                        p =createConfigurableProduct(r);
                       temp = name;
                       productConfigurables.add(p);
                       System.out.println(p.getTitle());
                   }
                   else{
                       createSimpleProduct(r);
                       if(allSize.isEmpty()){
                           getAllSize();
                       }
                       String value = getAdditionalAttributeValue();
                       pp_simple.setPrice(p.getPrice());
                       pp_simple.setSize(allSize.get(value));
                       p.addNewSimpleProduct(pp_simple);

                   }
                   Log.d("Products",r.toString());

               }
           }

         private String getAdditionalAttributeValue() throws IOException, XmlPullParserException {
            SoapObject attributes = new SoapObject(NAMESPACE,"attributes");
            SoapObject additional = new SoapObject(NAMESPACE,"additional_attributes");
            additional.addProperty("attribute","size");
            SoapObject requestAtributtes = new SoapObject(NAMESPACE,"catalogProductRequestAttributes");
            requestAtributtes.addProperty("attributes",attributes);
            requestAtributtes.addProperty("additional_attributes",additional);
            request = new SoapObject(NAMESPACE, "catalogProductInfo");
            request.addProperty("sessionId", sessionId);
            request.addProperty("productId", "8");
            request.addProperty("attributes",requestAtributtes);
            env.setOutputSoapObject(request);
            androidHttpTransport.call("", env);
            r = (SoapObject) env.getResponse();
            SoapObject result = (SoapObject)r.getProperty(r.getPropertyCount()-1);
            result= (SoapObject)result.getProperty("item");
            String value = (String)result.getProperty("value");

            return value;
         }


      /*get the attribute size with the respect label and value, save them for future use*/
      private void getAllSize() throws IOException, XmlPullParserException {
          request = new SoapObject(NAMESPACE, "catalogProductAttributeInfo");
          request.addProperty("sessionId", sessionId);
          request.addProperty("attribute","size");
          env.setOutputSoapObject(request);
          androidHttpTransport.call("", env);
          r = (SoapObject) env.getResponse();
          SoapObject sizeOptions = (SoapObject)r.getProperty("options");
          for(int i =0;i<sizeOptions.getPropertyCount();i++) {
              SoapObject item = (SoapObject) sizeOptions.getProperty(i);
              allSize.put((String)item.getProperty("value"),(String)item.getProperty("label"));
          }
      }
           private void createSimpleProduct(SoapObject r) {
                    pp_simple.setProduct_id((String)r.getProperty("product_id"));
                    pp_simple.setSku((String)r.getProperty("sku"));
                    pp_simple.setTitle((String)r.getProperty("name"));
           }

           private ProductConfigurable createConfigurableProduct(SoapObject r) {
               ProductConfigurable p = new ProductConfigurable();
               p.setProduct_id((String)r.getProperty("product_id"));
               p.setSku((String)r.getProperty("sku"));
               p.setTitle((String)r.getProperty("name"));
               p.setDescription((String)r.getProperty("description"));
               Double price=Double.parseDouble((String)r.getProperty("price"));
               String pr = String.format("%.2f",price);
               p.setPrice(pr);
               return p;
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
               for(ProductConfigurable p:productConfigurables) {
                  // System.out.println(p);
               }
               createItemList();
               pDialog.dismiss();

           }


       }

    private void createItemList(){


        String[] name = new String[]{"VOLCOM SUPERNATURAL INS JACKET ELECTRIC GREEN - 2015", "BURTON WOMAN SOCIETY PANT BLUE-RAY NOVEAU NEON - 2014","BURTON WB PELE MITT KAMANA WANNA LEI YA - 2015","SALOMON ASSASSIN 155 - 2015"};
        String[] price = new String[]{"256.5€", "120.95€","34.97€","84.00€"};
        String[] description = new String[]{"Great Jacket", "Comfortable snowPants","LowPrice, good quality gloves","New model of SnowBoard with incredible price"};
        int[] images = new int[]{R.drawable.jacket,R.drawable.pant2,R.drawable.gloves,R.drawable.board};

      /*  for(int i =0;i<name.length;i++) {
            ProductConfigurable p = new ProductConfigurable();
            p.setTitle(name[i]);
            p.setImage(images[i]);
            p.setPrice(price[i]);
            p.setDescription(description[i]);
            productConfigurables.add(p);
        }*/

        ListView oferta = (ListView)findViewById(R.id.top_seller_list);
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
