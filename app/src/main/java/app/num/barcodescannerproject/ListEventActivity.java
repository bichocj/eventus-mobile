package app.num.barcodescannerproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Adapters.EventAdapter;
import Objects.ActivityResponse;
import Objects.AttendanceClass;
import Objects.AttendanceRequest;
import Objects.EventResponse;
import Objects.LogInResponse;
import Objects.RegisterResponse;
import Objects.SyncDbObj;
import Objects.SyncObject;
import Objects.User;
import Utils.NetworkStatus;
import dbapp.SqlliteConsulter;
import settings.Global_Variables;

/**
 * Created by Administrador on 18/07/2016.
 */
public class ListEventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String token;
    private String static_url;
    private NetworkStatus networkstatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkstatus=new NetworkStatus(ListEventActivity.this);
        if (Global_Variables.DEV) {
            static_url = Global_Variables.DEV_STATIC_URL;
        } else {
            static_url = Global_Variables.PROD_STATIC_URL;
        }
        setContentView(R.layout.v_list_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        display_username();
        showEvent();
    }
    public void display_username(){
        LayoutInflater inflater = getLayoutInflater();
        View view=inflater.inflate(R.layout.nav_header_event_list,null);
        TextView textView = (TextView) view.findViewById(R.id.user_name_nav_bar);
        SqlliteConsulter MDB= new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
        User user=MDB.isLoggedUser();
        if (user != null) {
            textView.setText(user.getUsername());
        }
    }
    public void showEvent(){
        final SqlliteConsulter MDB= new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
        final ListView lv= (ListView)findViewById(R.id.Event_lstview);
        ArrayList<EventResponse> eventResponseList= MDB.showEvent();
        if(eventResponseList!=null) {
            EventAdapter adapter= new EventAdapter(ListEventActivity.this,0,eventResponseList);
            lv.setAdapter(adapter);

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final LayoutInflater inflater = getLayoutInflater();
                ImageView info_view=(ImageView) view.findViewById(R.id.Info);
                info_view.setOnClickListener(new View.OnClickListener() {
                    //EventResponse s=lEvents.get(position);
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListEventActivity.this);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView=inflater.inflate(R.layout.event_details_toast,null);
                        dialogBuilder.setView(dialogView);
                        EventResponse item = (EventResponse) lv.getItemAtPosition(position);
                        TextView textView=(TextView) dialogView.findViewById(R.id.title_event_toast);
                        textView.setText(item.getSlug());

                        TextView textView1=(TextView) dialogView.findViewById(R.id.full_name_event);
                        textView1.setText(item.getName());

                        TextView textView2=(TextView) dialogView.findViewById(R.id.event_domain);
                        textView2.setText(item.getDomain());

                        if ((!(item.getLocalitation().equals("")) && !(item.getLocalitation().equals(null)))&&(!(item.getAddress().equals("")) || !(item.getAddress().equals(null)))) {
                            TextView textView3 = (TextView) dialogView.findViewById(R.id.location_event);
                            textView3.setText(item.getLocalitation()+"/"+item.getAddress());
                            textView3.setVisibility(View.VISIBLE);
                        }
                        if (!(item.getStart_at().equals("")) && !(item.getStart_at().equals(null))) {
                            TextView textView4 = (TextView) dialogView.findViewById(R.id.date_event);
                            textView4.setText(item.getStart_at());
                            textView4.setVisibility(View.VISIBLE);
                        }
                        TextView textView5=(TextView)dialogView.findViewById(R.id.success_btn_toast);

                        final AlertDialog alertDialog=dialogBuilder.create();
                        textView5.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
                EventResponse item = (EventResponse) lv.getItemAtPosition(position);
                Intent intent = new Intent(ListEventActivity.this, ListActivitiesActivity.class);
                intent.putExtra("pkEvent",item.getPk());
                intent.putExtra("nameEvent",item.getSlug());
                ListEventActivity.this.finish();
                startActivity(intent);
            }
        });

    }
    /* Modificar lista con lo devuelto por la consulta
     if(products!=null) {
            List<String> list = new ArrayList<String>();
            for (Product product : products) {
                list.add("Name: "+product.getName()+"\nPrice: "+product.getCost());
            }
            ArrayAdapter<String> adapter= new ArrayAdapter<String>(MainActivity.this.getApplicationContext(),android.R.layout.simple_list_item_1,list);
            lstProcducts.setAdapter(adapter);
        }
     */
    public void mxonclic(View view)
    {
        Intent intent= new Intent(this,EventActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_event_list,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.reload:
                setContentView(R.layout.v_list_events);
                showEvent();
                Toast.makeText(ListEventActivity.this,"Refrescado",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        final SqlliteConsulter MDB= new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
        if (id == R.id.list_events) {
            // Handle the camera action
            DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawers();
        }else if(id == R.id.close_session) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_exit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MDB.logoutDB();
                            ListEventActivity.this.finish();

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    }).show();

        }else if(id==R.id.configurations){
            if (networkstatus.isNetworkAvailable()) {
                User user=MDB.isLoggedUser();
                new HttpRequestFrequentlySync(user.getToken()).execute();
            }
            else {
                Toast.makeText(ListEventActivity.this,"No tiene conexion a internet",Toast.LENGTH_SHORT).show();


            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class HttpRequestFrequentlySync extends AsyncTask<Void, Void, SyncObject> {
        private String token;
        private Boolean syn_obj_null;
        private ListEventActivity activity;
        private SqlliteConsulter MDB= new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
        private String ErrorMessage;
        private ProgressDialog progressBar;
        HttpRequestFrequentlySync(String token) {
            this.token=token;
            progressBar = new ProgressDialog(ListEventActivity.this);

        }

        @Override
        protected SyncObject doInBackground(Void... params) {
            try {
                activity=ListEventActivity.this;
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar = ProgressDialog.show(ListEventActivity.this, "Espere por favor ...", "Sincronizando información...", true);
                        progressBar.setCancelable(false);
                    }});
                String url = static_url+"services/data/download/";
                SyncDbObj syncDbObj=MDB.get_last_sync_success();
                syn_obj_null=false;
                if(syncDbObj == null){
                    Intent intent =new Intent(ListEventActivity.this,SyncActivity.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                    ListEventActivity.this.finish();
                    syn_obj_null=true;
                    return null;


                }
                else {
                    String startDate = "";
                    Date startAt;
                    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH);
                    try {
                        Date formattedDate = dateFormat.parse(syncDbObj.getDate().toString());
                        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        startDate = dateFormatGmt.format(formattedDate);
                    } catch (ParseException parseEx) {
                        parseEx.printStackTrace();
                    }

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
                    requestHeaders.setContentType(new MediaType("application", "json"));
                    requestHeaders.add("token", token);
                    url+="?estimate=false&last_sync="+startDate;
                    org.springframework.http.HttpEntity requestEntity = new org.springframework.http.HttpEntity(requestHeaders);
                    ResponseEntity<SyncObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SyncObject.class);
                    SyncObject result = responseEntity.getBody();
                    if (result.getEvents().size() > 0 || result.getActivities().size() > 0 || result.getRegisters().size() > 0) {
                        MDB.AddEvent(result.getEvents());
                        MDB.AddRegister(result.getRegisters());
                        MDB.AddActivity(result.getActivities());
                        MDB.insertActualization(true);
                    }

                    //HttpResponse result=httpClient.execute(httpRequest);
                    return result;
                }
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
                new HttpRequestAsistantWeb(token).execute();
                progressBar.dismiss();
            }
            else
            {
                if (!syn_obj_null){
                    MDB.insertActualization(false);
                    new HttpRequestAsistantWeb(token).execute();
                    progressBar.dismiss();
                }
            }


        }
    }
    private class HttpRequestAsistantWeb extends AsyncTask<Void, Void, String> {
        private String token;
        private List<ActivityResponse> activityResponses;
        private List<EventResponse> eventResponses;
        private List<AttendanceClass> attendances;
        private List<RegisterResponse> registerResponses;
        private ListEventActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestAsistantWeb(String token) {
            this.token = token;
            progressBar = new ProgressDialog(ListEventActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            activity = ListEventActivity.this;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressBar = ProgressDialog.show(ListEventActivity.this, "Espere ...", "Sincronizando con Servidor ...", true);
                    progressBar.setCancelable(false);
                }
            });
            String state = "";
            SqlliteConsulter MDB = new SqlliteConsulter(ListEventActivity.this.getApplicationContext());
            attendances = MDB.getAttendance();
            eventResponses = MDB.showEvent();
            if (attendances != null) {
                if (attendances.size() == 0) {
                    return state = "None";
                } else {
                    for (EventResponse event : eventResponses) {
                        for (AttendanceClass attendace : attendances) {
                            try {
                                final String url = static_url + "services/" + event.getSlug() + "/v1/activities/" + attendace.getActivity() + "/attendaces/";
                                //Create an HTTP client
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
                                requestHeaders.setContentType(new MediaType("application", "json"));
                                requestHeaders.add("token", token);
                                AttendanceRequest attendanceRequest = new AttendanceRequest();
                                attendanceRequest.setRegister(attendace.getRegister());
                                HttpEntity requestEntity = new HttpEntity(attendanceRequest, requestHeaders);
                                ResponseEntity<AttendanceClass> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AttendanceClass.class);
                                AttendanceClass result = responseEntity.getBody();
                                HttpStatus statusLine = responseEntity.getStatusCode();
                                if (statusLine.value() == 201 || statusLine.value() == 302 || statusLine.value() == 200) {
                                    state = "OK";

                                    MDB.updateAttendance(attendace.getPk());

                                } else {
                                    Log.e("SingACtitvity", "Server responded with status code: " + statusLine.value());
                                    state = "FAIL";
                                    //failedLoadingPosts();
                                }
                            } catch (Exception e) {
                                Log.e("SingACtitvity", e.getMessage(), e);
                                state = "FAIL";
                                //failedLoadingPosts();
                            }


                        }
                    }
                }
            }
            return state;
        }

        @Override
        protected void onPostExecute(String activityResponses) {

                if (activityResponses.equals("OK") || activityResponses.equals("None")) {
                    Toast.makeText(ListEventActivity.this, "Se sincronizo correctamente. . .", Toast.LENGTH_LONG).show();
                    ListEventActivity.this.finish();
                    Intent intent=new Intent(ListEventActivity.this,ListEventActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ListEventActivity.this, "ERROR: No se sincronizaron los datos al server correctamente. . .", Toast.LENGTH_LONG).show();
                }

            progressBar.dismiss();

        }
    }
}
