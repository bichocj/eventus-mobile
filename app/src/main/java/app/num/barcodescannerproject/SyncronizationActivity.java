package app.num.barcodescannerproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Objects.ActivityResponse;
import Objects.AttendanceClass;
import Objects.EventResponse;
import Objects.Register;
import Objects.RegisterResponse;
import Objects.SyncObject;
import Objects.User;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import dbapp.SqlliteConsulter;
import settings.Global_Variables;

/**
 * Created by Administrador on 30/07/2016.
 */
public class SyncronizationActivity extends AppCompatActivity {
    private String token;
    private User user;
    private String static_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Global_Variables.DEV){
            static_url=Global_Variables.DEV_STATIC_URL;
        }
        else{
            static_url=Global_Variables.PROD_STATIC_URL;
        }
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            token=extras.getString("token");
        }
        setContentView(R.layout.v_sync);
        new HttpRequestFirstSync(token).execute();
    }
    void mxSyncLocal(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_sync_local)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
                        MDB.deleteAllDB();
                        //new HttpRequestEvent(token).execute();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();

    }
    void mxSyncWeb (View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_sync_web)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new HttpRequestAsistantWeb(token).execute();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //MenuInflater inflater=getMenuInflater();
        //inflater.inflate(R.menu.menu_sync_view,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
        switch (item.getItemId()) {
            case R.id.back:
                if(user.getLogueado().equals("true")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.dialog_exit)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MDB.logoutDB();
                                    SyncronizationActivity.this.finish();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            }).show();
                }


                break;
            case R.id.continue_nav:
                if(MDB.getRegisterQuantityAll() > 0) {
                    this.finish();
                    Intent intent = new Intent(SyncronizationActivity.this, ListEventActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(SyncronizationActivity.this,"Descargue Informacion del evento",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.reload:
                this.finish();
                Intent intents = new Intent(SyncronizationActivity.this, SyncronizationActivity.class);
                startActivity(intents);
                Toast.makeText(SyncronizationActivity.this,"Refrescado",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class HttpRequestAsistantWeb extends AsyncTask<Void, Void, String> {
        private String token;
        private List<ActivityResponse> activityResponses;
        private List<EventResponse> eventResponses;
        private List<AttendanceClass> attendances;
        private List<RegisterResponse>registerResponses;
        private SyncronizationActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestAsistantWeb(String token) {
            this.token=token;
            progressBar = new ProgressDialog(SyncronizationActivity.this);
        }

        @Override
        protected String  doInBackground(Void... params) {
            activity=SyncronizationActivity.this;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressBar = ProgressDialog.show(SyncronizationActivity.this, "Please wait ...", "Sincronizando con Servidor ...", true);
                    progressBar.setCancelable(false);
                }});
            String state="";
            SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
            attendances=MDB.getAttendance();
            if(attendances != null ) {
                if (attendances.size() == 0){
                    return state="None";
                }
                else {
                    for (AttendanceClass attendace : attendances) {
                        try {
                            final String url = static_url+"services/cigunsa/activities/" + attendace.getActivity() + "/registers/" + attendace.getRegister() + "/attendances/";
                            //Create an HTTP client
                            HttpClient client = new DefaultHttpClient();
                            HttpPost post = new HttpPost(url);
                            post.setHeader("token", token);
                            //Perform the request and check the status code
                            HttpResponse response = client.execute(post);
                            StatusLine statusLine = response.getStatusLine();
                            if (statusLine.getStatusCode() == 201 || statusLine.getStatusCode() == 302) {
                                state = "OK";
                            } else {
                                Log.e("SingACtitvity", "Server responded with status code: " + statusLine.getStatusCode());
                                state = "FAIL";
                                return state;
                                //failedLoadingPosts();
                            }
                        } catch (Exception e) {
                            Log.e("SingACtitvity", e.getMessage(), e);
                            state = "FAIL";
                            return state;
                            //failedLoadingPosts();
                        }


                    }
                }
            }
            return state;
        }
        @Override
        protected void onPostExecute(String activityResponses)
        {
            if (activityResponses.equals("None")){
                Toast.makeText(SyncronizationActivity.this,"No tomo asistencias en su movil. . .",Toast.LENGTH_LONG).show();
            }
            else {
                if (activityResponses.equals("OK")) {
                    Toast.makeText(SyncronizationActivity.this, "Se enviaron los datos al server correctamente. . .", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SyncronizationActivity.this, "ERROR: No se enviaron los datos al server correctamente. . .", Toast.LENGTH_LONG).show();
                }
            }
            progressBar.dismiss();

        }
    }

    private class HttpRequestFirstSync extends AsyncTask<Void, Void, SyncObject> {
        private String token;
        private SyncronizationActivity activity;
        private SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
        private String ErrorMessage;
        private ProgressDialog progressBar;
        HttpRequestFirstSync(String token) {
            this.token=token;
            progressBar = new ProgressDialog(SyncronizationActivity.this);

        }

        @Override
        protected SyncObject doInBackground(Void... params) {
            try {
                activity=SyncronizationActivity.this;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar = ProgressDialog.show(SyncronizationActivity.this, "Espere por favor ...", "Descargando información...", true);
                        progressBar.setCancelable(false);
                    }});
                final String url = static_url+"services/data/download/";

                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpHeaders requestHeaders = new HttpHeaders();
                //DefaultHttpClient httpClient = new DefaultHttpClient();
                //HttpPost httpRequest = new HttpPost(url);
                /*httpRequest.setEntity(new StringEntity("{\"estimate\":false}"));
                httpRequest.setHeader("content-type", "application/json");
                httpRequest.setHeader("token",token);*/
                requestHeaders.setContentType(new MediaType("application","json"));
                requestHeaders.add("token",token);
                MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
                body.add("estimate","false");
                org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(body,requestHeaders);
                ResponseEntity<SyncObject> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,SyncObject.class);
                SyncObject result = responseEntity.getBody();

                MDB.AddEvent(result.getEvents());
                MDB.AddActivity(result.getActivities());
                MDB.AddRegister(result.getRegisters());
                MDB.insertActualization(true);

                //HttpResponse result=httpClient.execute(httpRequest);
                return result;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                ErrorMessage=e.getMessage();

                return null;
            }
        }
        @Override
        protected void onPostExecute(SyncObject greeting)
        {
            if(greeting!=null) {
                Intent intent = new Intent(SyncronizationActivity.this, ListEventActivity.class);
                SyncronizationActivity.this.finish();
                startActivity(intent);
                progressBar.dismiss();
            }
            else
            {
                MDB.insertActualization(false);
                SyncronizationActivity.this.finish();
                Toast.makeText(activity,"Error al descargar información",Toast.LENGTH_SHORT).show();
            }
            progressBar.dismiss();
        }
    }

}
