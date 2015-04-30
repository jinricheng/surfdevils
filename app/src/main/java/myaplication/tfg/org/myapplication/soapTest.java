package myaplication.tfg.org.myapplication;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class soapTest extends ActionBarActivity {
   private class DownLoad extends AsyncTask<String,Float,String> {
        ProgressDialog pDialog;
        SoapObject r;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Hi", "Download Commencing");

            pDialog = new ProgressDialog(soapTest.this);

            String message= "Esperando...";
            SpannableString ss2 =  new SpannableString(message);
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);
            pDialog.setMessage(ss2);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            //INSERT YOUR FUNCTION CALL HERE
             String NAMESPACE = "urn:Magento";
             String URL = "http://mininegocio.es/api/v2_soap/";

            try {
                SoapSerializationEnvelope env = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                env.dotNet = false;
                env.xsd = SoapSerializationEnvelope.XSD;
                env.enc = SoapSerializationEnvelope.ENC;
                SoapObject request = new SoapObject(NAMESPACE, "login");
                request.addProperty("username", "jin");
                request.addProperty("apiKey", "1234567890");
                env.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call("", env);
                Object result = env.getResponse();
                Log.d("sessionId", result.toString());

                //making call to get list of customers

                String sessionId = result.toString();

                request = new SoapObject(NAMESPACE, "catalogProductList");
 /*               StringArraySerializer stringArray = new StringArraySerializer();
                stringArray.add("7");
                stringArray.add("8");
                PropertyInfo stringArrayProperty = new PropertyInfo();
                stringArrayProperty.setName("products");
                stringArrayProperty.setValue(stringArray);
                stringArrayProperty.setType(stringArray.getClass());
*/
                request.addProperty("sessionId",sessionId );
              //  request.addProperty(stringArrayProperty);
                env.setOutputSoapObject(request);
                androidHttpTransport.call("", env);

               r = (SoapObject)env.getResponse();

                    for(int i =0;i<r.getPropertyCount();i++) {

                        SoapObject child=(SoapObject) r.getProperty(i);
                        Log.d("result",child.toString());
                        Log.d("name",child.getProperty("name").toString());


                    }

     /*           for(int i =0; i<r.getPropertyCount();i++) {
                    String resul = r.getProperty(i).toString();
                    int start = resul.indexOf("{");
                    int end = resul.indexOf("}");
                    String sub = resul.substring(start + 1, end);
                    Log.d("catalogProductInfo", sub);
                }
*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed!";

        }


       protected void onProgressUpdate (Float... valores) {
           int p = Math.round(100*valores[0]);
           pDialog.setProgress(p);
       }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Hi", "Done Downloading.");

            pDialog.dismiss();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap_test);
        DownLoad downLoad = new DownLoad();
        downLoad.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_soap_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
