package app.num.barcodescannerproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adapters.RegisterAdapter;
import Objects.ActivityResponse;
import Objects.AttendanceClass;
import Objects.AttendanceRequest;
import Objects.EventResponse;
import Objects.RegisterResponse;
import Objects.User;
import Utils.NetworkStatus;
import dbapp.SqlliteConsulter;
import settings.Global_Variables;

/**
 * Created by Administrador on 20/07/2016.
 */
public class ListAsistansActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {
    private String pkEvent;
    private SqlliteConsulter MDB;
    private String pkActivity;
    private String nameActivity;
    private String nameEvent;
    private String static_url;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private NetworkStatus networkstatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkstatus=new NetworkStatus(ListAsistansActivity.this);
        if (Global_Variables.DEV) {
            static_url = Global_Variables.DEV_STATIC_URL;
        } else {
            static_url = Global_Variables.PROD_STATIC_URL;
        }
        Bundle extras = getIntent().getExtras();
        MDB = new SqlliteConsulter(ListAsistansActivity.this.getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (extras != null) {
            pkEvent = extras.getString("pkEvent");
            pkActivity = extras.getString("pkActivity");
            nameActivity = extras.getString("nameActivity");
            nameEvent = extras.getString("nameEvent");
            //getSupportActionBar().setTitle(nameActivity);
        }
        setContentView(R.layout.v_list_asistants);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.Coodinator_Attendance);
        if (MDB.ExistAttendanceWithOutSync()) {
            int attendaceQ = MDB.getAttendanceQuantity(pkActivity);
            int registers = MDB.getRegisterQuantity(pkActivity);
            String quantityAttendance = attendaceQ >= 0 ? String.valueOf(attendaceQ) : "--";
            String quantityRegister = registers >= 0 ? String.valueOf(registers) : "--";
            Snackbar.make(coordinatorLayout, quantityAttendance + '/' + quantityRegister, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Sincronizar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (networkstatus.isNetworkAvailable()) {
                                User user = MDB.isLoggedUser();
                                new HttpRequestAsistantWeb(user.getToken()).execute();
                            }
                            else {
                                Intent intent=new Intent(ListAsistansActivity.this,ListAsistansActivity.class);
                                intent.putExtra("pkEvent",pkEvent);
                                intent.putExtra("pkActivity",pkActivity);
                                intent.putExtra("nameActivity",nameActivity);
                                intent.putExtra("nameEvent",nameEvent);
                                startActivity(intent);
                                ListAsistansActivity.this.finish();
                                Toast.makeText(ListAsistansActivity.this,"No tiene conexion a internet",Toast.LENGTH_SHORT).show();


                            }


                        }
                    }).show();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_tool);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(ListAsistansActivity.this, ScanQrActivity.class);
                intent.putExtra("pkEvent", pkEvent);
                intent.putExtra("pkActivity", pkActivity);
                intent.putExtra("nameEvent", nameEvent);
                intent.putExtra("nameActivity", nameActivity);
                intent.putExtra("list", true);
                startActivity(intent);
                ListAsistansActivity.this.finish();
            }
        });
        showAssits();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    private void showAssits() {

        ArrayList<RegisterResponse> registerResponses1 = MDB.getRegisters(pkEvent, pkActivity);
        for (RegisterResponse register : registerResponses1) {
            if (MDB.have_attendance(pkActivity, register.getPk()))
                register.setHave_attendance("true");
            else
                register.setHave_attendance("false");
        }
        final ListView lv = (ListView) findViewById(R.id.Asistants_lv);
        List<RegisterResponse> registerResponses = registerResponses1;
//        for(RegisterResponse registerResponse : registerResponses){
//            registerResponse.setFirst_name(registerResponse.getFirst_name().substring(0,1).toUpperCase()+registerResponse.getFirst_name().substring(1));
//        }
        Collections.sort(registerResponses, new Comparator<RegisterResponse>() {
            @Override
            public int compare(RegisterResponse lhs, RegisterResponse rhs) {
                return lhs.getLast_name().toUpperCase().compareTo(rhs.getLast_name().toUpperCase());
            }
        });
        lv.setFastScrollEnabled(true);
        lv.setTextFilterEnabled(true);
        lv.setFastScrollAlwaysVisible(true);

        if (registerResponses != null) {
            RegisterAdapter adapter = new RegisterAdapter(ListAsistansActivity.this, 0, registerResponses);
            lv.setAdapter(adapter);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RegisterResponse item = (RegisterResponse) lv.getItemAtPosition(position);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ListAsistansActivity.this);
                builder.setMessage("Tomar asistencias a: "+item.getFirst_name()+" "+item.getLast_name())
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String register=MDB.TakeAssistance(item.getPk(),pkActivity,false);
                                Toast.makeText(ListAsistansActivity.this, register, Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(ListAsistansActivity.this,ListAsistansActivity.class);
                                intent.putExtra("pkEvent",pkEvent);
                                intent.putExtra("pkActivity",pkActivity);
                                intent.putExtra("nameActivity",nameActivity);
                                intent.putExtra("nameEvent",nameEvent);
                                ListAsistansActivity.this.finish();
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_asistant_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Intent intent;
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                intent = new Intent(this, ListActivitiesActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("pkEvent",pkEvent);
                intent.putExtra("nameEvent",nameEvent);
                startActivity(intent);
                break;*/
            /*case R.id.scanqr:
                this.finish();
                intent= new Intent(this,ScanQrActivity.class);
                intent.putExtra("pkActivity",pkActivity);
                intent.putExtra("list",true);
                startActivity(intent);
                break;*/
            /*default:
                return super.onOptionsItemSelected(item);
        }*/

        if (item.getItemId() == android.R.id.home) {
            ListAsistansActivity.this.finish();
            Intent intent = new Intent(ListAsistansActivity.this, ListActivitiesActivity.class);
            intent.putExtra("pkEvent", pkEvent);
            intent.putExtra("nameEvent", nameEvent);
            intent.putExtra("pkActivity", pkActivity);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListAsistans Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.android.emeshattendace/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ListAsistans Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.android.emeshattendace/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class HttpRequestAsistantWeb extends AsyncTask<Void, Void, String> {
        private String token;
        private List<ActivityResponse> activityResponses;
        private List<EventResponse> eventResponses;
        private List<AttendanceClass> attendances;
        private List<RegisterResponse> registerResponses;
        private ListAsistansActivity activity;
        private String ErrorMessage;
        private ProgressDialog progressBar;

        HttpRequestAsistantWeb(String token) {
            this.token = token;
            progressBar = new ProgressDialog(ListAsistansActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            activity = ListAsistansActivity.this;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    progressBar = ProgressDialog.show(ListAsistansActivity.this, "Espere ...", "Sincronizando con Servidor ...", true);
                    progressBar.setCancelable(false);
                }
            });
            String state = "";
            SqlliteConsulter MDB = new SqlliteConsulter(ListAsistansActivity.this.getApplicationContext());
            eventResponses = MDB.showEvent();
            attendances = MDB.getAttendance();
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
        protected void onPostExecute(String serverResponse) {
            if (serverResponse.equals("None")) {
                Toast.makeText(ListAsistansActivity.this, "No tomo asistencias en su movil. . .", Toast.LENGTH_LONG).show();
            } else {
                if (serverResponse.equals("OK")) {
                    Toast.makeText(ListAsistansActivity.this, "Se enviaron los datos al server correctamente. . .", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ListAsistansActivity.this, "Por favor inténtelo nuevamente ...", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ListAsistansActivity.this,ListAsistansActivity.class);
                    intent.putExtra("pkEvent",pkEvent);
                    intent.putExtra("pkActivity",pkActivity);
                    intent.putExtra("nameActivity",nameActivity);
                    intent.putExtra("nameEvent",nameEvent);
                    startActivity(intent);
                    ListAsistansActivity.this.finish();
                }
            }
            progressBar.dismiss();

        }
    }
}
