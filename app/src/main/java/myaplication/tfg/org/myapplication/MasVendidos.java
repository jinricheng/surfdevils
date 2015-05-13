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
import android.widget.ImageView;
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
import java.util.Collection;
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
    private String section;
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

        productConfigurables = new ArrayList<ProductConfigurable>();
        product_id = new ArrayList<String>();
        pp_simple =new ProductSimple();
        allSize = new HashMap<String,String>();

    }

    /*initialize the activity title*/
    private void getActivityTitle() {
        Bundle bundle = getIntent().getExtras();
        String title = (String)bundle.get("name");
        TextView text= (TextView)findViewById(R.id.special_title);

        final LayoutInflater factory = getLayoutInflater();
        final View view= factory.inflate(R.layout.topseller_list, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.special_icon);
        text.setText(title);
        section=title;
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
               String message = "Esperando...";
               SpannableString ss2 = new SpannableString(message);
               ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
               ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
               pDialog.setMessage(ss2);
               pDialog.setCancelable(true);
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

                  // getAllSize();
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
          sessionId = (String)result;
      }

           private void getAllListItem() throws IOException, XmlPullParserException {
               request = new SoapObject(NAMESPACE, "catalogProductList");
               request.addProperty("sessionId", sessionId);
               env.setOutputSoapObject(request);
               androidHttpTransport.call("", env);
               r = (SoapObject) env.getResponse();
               Log.d("products",r.toString());
               String temp = new String();
               for (int i = 0; i < r.getPropertyCount(); i++) {
                   SoapObject child = (SoapObject) r.getProperty(i);
                    String name =(String)child.getProperty("name");
                    String type = (String)child.getProperty("type");
                   if(!name.equals(temp) && type.equals("configurable")){
                       p_configurable = createConfigurableProduct(child);
                       productConfigurables.add(p_configurable);
                   }
                   else{
                       //pp_simple = createSimpleProduct(child);
                       p_configurable.addSimpleProductId((String)child.getProperty("product_id"));
                   }
               }
               getIndividualProductInfo();
           }

           public void getIndividualProductInfo() throws IOException, XmlPullParserException {
               String temp =new String();
               ProductConfigurable p = new ProductConfigurable();
               for (int i =0;i< productConfigurables.size();i++) {
                   p = productConfigurables.get(i);
                   request = new SoapObject(NAMESPACE, "catalogProductInfo");
                   request.addProperty("sessionId", sessionId);
                   request.addProperty("productId", p.getProduct_id());
                   env.setOutputSoapObject(request);
                   androidHttpTransport.call("", env);
                   r = (SoapObject) env.getResponse();
                   Double price=Double.parseDouble((String)r.getProperty("price"));
                   String pr = String.format("%.2f",price);
                   p.setPrice(pr);
                   p.setDescription((String) r.getProperty("description"));
                   p.setSection(section);
                   productConfigurables.set(i, p);
                   Log.d("info",p.toString());
               }

           }
         private ProductConfigurable createConfigurableProduct(SoapObject r) throws IOException, XmlPullParserException {
            ProductConfigurable p = new ProductConfigurable();
            p.setProduct_id((String) r.getProperty("product_id"));
            p.setSku((String) r.getProperty("sku"));
            p.setTitle((String) r.getProperty("name"));
            String imageUrl = "http://mininegocio.es/media/catalog/product/android/"+p.getProduct_id()+".jpg";
            p.setImage(imageUrl);
            Log.d("imageUrl",imageUrl);
            return p;}
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
           private ProductSimple createSimpleProduct(SoapObject r) {
               ProductSimple p = new ProductSimple();
               p.setProduct_id((String) r.getProperty("product_id"));
               p.setSku((String) r.getProperty("sku"));
               p.setTitle((String) r.getProperty("name"));
               return p;
           }


          /*     p.setDescription((String)r.getProperty("description"));
               Double price=Double.parseDouble((String)r.getProperty("price"));
               String pr = String.format("%.2f",price);
             //  String ImageUrl= getProductImage(p.getProduct_id());
               p.setPrice(pr);
               return p;*/


      private String getProductImage(String product_id) throws IOException, XmlPullParserException {
          request = new SoapObject(NAMESPACE, "catalogProductAttributeMediaList");
          request.addProperty("sessionId", sessionId);
          request.addProperty("productId", "7");
          env.setOutputSoapObject(request);
          androidHttpTransport.call("", env);
          r = (SoapObject) env.getResponse();
          Log.d("ImageUrl", r.toString());
          return r.toString();
      }

      protected void onProgressUpdate(Float... valores) {
               int p = Math.round(100 * valores[0]);
               pDialog.setProgress(p);
           }

           @Override
           protected void onPostExecute(String result) {
               super.onPostExecute(result);

               Log.d("Hi", "Done Downloading.");

               createItemList();
               pDialog.dismiss();

           }


       }

    private void createItemList(){
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
