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
import Objects.User;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 30/07/2016.
 */
public class SyncronizationActivity extends AppCompatActivity {
    private String token;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras= getIntent().getExtras();
        if(extras != null){
            token=extras.getString("token");
        }
        SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
        user=MDB.isLoggedUser();
        if(user!=null)
        {
            this.token=user.getToken();
        }



        setContentView(R.layout.v_sync);
        if(user.getSync_local().equals("true")) {
            Button button_syclbd = (Button) findViewById(R.id.syndb_btn);
            button_syclbd.setVisibility(View.GONE);

        }
        if(user.getSync_web().equals("true")){
            Button button_syclw= (Button) findViewById(R.id.syncwb_btn);
            button_syclw.setVisibility(View.GONE);
        }
        if(user.getSync_local().equals("true") && user.getSync_web().equals("true")){
            Intent intent = new Intent(SyncronizationActivity.this, ListEventActivity.class);
            startActivity(intent);
        }
    }
    void mxSyncLocal(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_sync_local)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
                        MDB.deleteAllDB();
                        new HttpRequestEvent(token).execute();

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
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_sync_view,menu);
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
                            final String url = "http://eventus.la//services//cigunsa//activities//" + attendace.getActivity() + "//registers//" + attendace.getRegister() + "//attendances//";
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

    private class HttpRequestEvent extends AsyncTask<Void, Void, EventResponse> {
        private String token;
        private SyncronizationActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;
        HttpRequestEvent(String token) {
            this.token=token;
            progressBar = new ProgressDialog(SyncronizationActivity.this);

        }

        @Override
        protected EventResponse doInBackground(Void... params) {
            try {
                activity=SyncronizationActivity.this;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar = ProgressDialog.show(SyncronizationActivity.this, "Please wait ...", "Sincronizando con eventos ...", true);
                        progressBar.setCancelable(false);
                    }});
                final String url = "http://eventus.la/services/event/71";

                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                requestHeaders.add("token",token);
                org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(requestHeaders);
                ResponseEntity<EventResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,EventResponse.class);
                EventResponse result = responseEntity.getBody();
                return result;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                ErrorMessage=e.getMessage();

                return null;
            }
        }
        @Override
        protected void onPostExecute(EventResponse greeting)
        {
            if(greeting!=null) {
                SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
                MDB.AddEvent(greeting);
                new HttpRequestActivity(token,greeting).execute();
            }
            else
            {
                Toast.makeText(activity,"Error al sincronizar eventos",Toast.LENGTH_SHORT).show();
            }
            progressBar.dismiss();
        }
    }
    private class HttpRequestActivity extends AsyncTask<Void, Void, List<ActivityResponse>> {
        private String token;
        private List<ActivityResponse> activityResponses;
        private EventResponse eventResponses;
        private SyncronizationActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestActivity(String token,EventResponse eventResponses) {
            this.token=token;
            this.eventResponses=eventResponses;
            progressBar = new ProgressDialog(SyncronizationActivity.this);

        }

        @Override
        protected List<ActivityResponse>  doInBackground(Void... params) {
            activity=SyncronizationActivity.this;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressBar = ProgressDialog.show(SyncronizationActivity.this, "Please wait ...", "Sincronizando con actividades ...", true);
                    progressBar.setCancelable(false);
                }});
            activityResponses= new ArrayList<ActivityResponse>();
            /*for (EventResponse eventResponse:eventResponses) {*/
                try {
                    final String url = "http://eventus.la//services//"+eventResponses.getSlug()+"//activities//?limit=1000";
                    //Create an HTTP client
                    HttpClient client = new DefaultHttpClient();
                    HttpGet post = new HttpGet(url);
                    post.setHeader("token",token);
                    //Perform the request and check the status code
                    HttpResponse response = client.execute(post);
                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        InputStream content = entity.getContent();

                        try {
                            //Read the server response and attempt to parse it as JSON
                            BufferedReader r = new BufferedReader(new InputStreamReader(content));
                            StringBuilder total = new StringBuilder();
                            String line;
                            while ((line = r.readLine()) != null) {
                                total.append(line).append('\n');
                            }

                            JSONArray start_object=new JSONArray(new JSONObject(total.toString()).getString("data"));
                            if(start_object.length()>0){
                                for (int i=0; i < start_object.length();i++) {
                                    JSONObject jsonObject = start_object.getJSONObject(i);
                                    ActivityResponse activityResponse = new ActivityResponse();
                                    activityResponse.setPk(jsonObject.getString("pk"));
                                    activityResponse.setName(jsonObject.getString("name"));
                                    activityResponse.setStart_at(jsonObject.getString("start_at"));
                                    activityResponse.setEnd_at(jsonObject.getString("end_at"));
                                    activityResponse.setAddress(jsonObject.getString("address"));
                                    activityResponse.setCapacity(jsonObject.getString("capacity"));
                                    activityResponse.setEvent(jsonObject.getString("event"));
                                    activityResponse.setSpeaker(jsonObject.getString("speaker"));
                                    activityResponse.setAvaliable(jsonObject.getString("avaliable"));
                                    activityResponse.setRegistered_number(jsonObject.getString("registered_number"));
                                    activityResponse.setAttendees(jsonObject.getString("attendees"));
                                    activityResponse.setInstitution(jsonObject.getString("institution"));
                                    JSONObject objectType = new JSONObject(jsonObject.getString("_type"));
                                    activityResponse.getType().setLowercased(objectType.getString("lowercased"));
                                    activityResponse.getType().setPk(objectType.getString("pk"));
                                    activityResponse.getType().setMax_per_user(objectType.getString("max_per_user"));
                                    activityResponse.getType().setColor(objectType.getString("color"));
                                    activityResponse.getType().setIs_grouping_by_institution(objectType.getString("is_grouping_by_institution"));
                                    JSONObject objectActivityType = new JSONObject(objectType.getString("activity_type"));
                                    activityResponse.getType().getActivity_type().setName(objectActivityType.getString("name"));
                                    activityResponse.getType().getActivity_type().setPk(objectActivityType.getString("pk"));
                                    activityResponses.add(activityResponse);
                                }
                            }


                        } catch (Exception e) {
                            Log.e("SingACtitvity", e.getMessage(), e);
                        }
                    } else {
                        Log.e("SingACtitvity", "Server responded with status code: " + statusLine.getStatusCode());
                        //failedLoadingPosts();
                    }
                } catch(Exception e) {
                    Log.e("SingACtitvity", e.getMessage(), e);
                    //failedLoadingPosts();
                }


            SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
            MDB.AddActivity(activityResponses);
            return activityResponses;
        }
        @Override
        protected void onPostExecute(List<ActivityResponse> activityResponses)
        {
            if(activityResponses!=null) {
                new HttpRequestRegisters(token,activityResponses,eventResponses).execute();

            }
            progressBar.dismiss();
        }
    }
    private class HttpRequestRegisters extends AsyncTask<Void, Void, List<RegisterResponse>> {
        private String token;
        private List<RegisterResponse>registerResponses;
        private List<ActivityResponse> activityResponses;
        private EventResponse eventResponses;
        private SyncronizationActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestRegisters(String token, List<ActivityResponse> activityResponses, EventResponse eventResponses) {
            this.token = token;
            this.activityResponses = activityResponses;
            this.eventResponses = eventResponses;
            progressBar = new ProgressDialog(SyncronizationActivity.this);

        }

        @Override
        protected List<RegisterResponse> doInBackground(Void... params) {
            activity=SyncronizationActivity.this;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressBar = ProgressDialog.show(SyncronizationActivity.this, "Please wait ...", "Sincronizando con Registrados ...", true);
                    progressBar.setCancelable(false);
                }});
            SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
            registerResponses=new ArrayList<RegisterResponse>();
            /*for (EventResponse eventResponse : eventResponses) {*/
                for (ActivityResponse activityResponse : activityResponses) {
                    if(activityResponse.getEvent().equals(eventResponses.getPk())) {
                        try {
                            final String url = "http://eventus.la//services//" + eventResponses.getSlug() + "//activities//"+activityResponse.getPk()+"//registers-movil//";
                            //Create an HTTP client
                            HttpClient client = new DefaultHttpClient();
                            HttpGet post = new HttpGet(url);
                            post.setHeader("token",token);
                            //Perform the request and check the status code
                            HttpResponse response = client.execute(post);
                            StatusLine statusLine = response.getStatusLine();
                            if(statusLine.getStatusCode() == 200) {
                                HttpEntity entity = response.getEntity();
                                InputStream content = entity.getContent();

                                try {
                                    //Read the server response and attempt to parse it as JSON
                                    BufferedReader r = new BufferedReader(new InputStreamReader(content));
                                    StringBuilder total = new StringBuilder();
                                    String line;
                                    while ((line = r.readLine()) != null) {
                                        total.append(line).append('\n');
                                    }

                                    JSONArray start_object=new JSONArray(new JSONObject(total.toString()).getString("data"));
                                    if(start_object.length()>0){
                                        for (int i=0; i < start_object.length();i++) {
                                            JSONObject jsonObject = start_object.getJSONObject(i);
                                            RegisterResponse registerResponse= new RegisterResponse();
                                            registerResponse.setPk(jsonObject.getString("pk"));
                                            registerResponse.setActivity(jsonObject.getString("activity"));
                                            registerResponse.setHave_attendance(jsonObject.getString("have_attendance"));
                                            JSONObject objectRegister = new JSONObject(jsonObject.getString("register"));
                                            registerResponse.getRegister().setPk(objectRegister.getString("pk"));
                                            JSONObject objectTicket = new JSONObject(objectRegister.getString("ticket"));
                                            registerResponse.getRegister().getTicket().setName(objectTicket.getString("name"));
                                            JSONObject objectPerson= new JSONObject(objectRegister.getString("person"));
                                            registerResponse.getRegister().getPerson().setFirst_name(objectPerson.getString("first_name"));
                                            registerResponse.getRegister().getPerson().setLast_name(objectPerson.getString("last_name"));
                                            registerResponses.add(registerResponse);
                                            if (registerResponse.getHave_attendance().equals("true")){
                                                MDB.AddAssitanceInSync(registerResponse.getActivity(),registerResponse.getRegister().getPk());
                                            }

                                        }
                                    }


                                } catch (Exception e) {
                                    Log.e("SingACtitvity", e.getMessage(), e);
                                }
                            } else {
                                Log.e("SingACtitvity", "Server responded with status code: " + statusLine.getStatusCode());
                                //failedLoadingPosts();
                            }
                        } catch(Exception e) {
                            Log.e("SingACtitvity", e.getMessage(), e);
                            //failedLoadingPosts();
                        }

                }
            }
            return registerResponses;
        }

        @Override
        protected void onPostExecute(List<RegisterResponse> registerResponses)
        {
            if(registerResponses!=null) {
                SqlliteConsulter MDB= new SqlliteConsulter(SyncronizationActivity.this.getApplicationContext());
                MDB.AddRegister(registerResponses);
                SyncronizationActivity.this.finish();
                Intent intent = new Intent(SyncronizationActivity.this, ListEventActivity.class);
                startActivity(intent);
                progressBar.dismiss();
            }
        }
    }
}
