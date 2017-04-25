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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import Adapters.EventAdapter;
import Objects.ActivityResponse;
import Objects.EventResponse;
import Objects.LogInResponse;
import Objects.User;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 18/07/2016.
 */
public class ListEventActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Toast.makeText(ListEventActivity.this, "List Event", Toast.LENGTH_LONG).show();
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

                        if ((item.getLocalitation().equals("") || item.getLocalitation().equals(null))&&((item.getAddress().equals("") || item.getAddress().equals(null)))) {
                            TextView textView3 = (TextView) dialogView.findViewById(R.id.location_event);
                            textView3.setText(item.getLocalitation()+"/"+item.getAddress());
                            textView3.setVisibility(View.VISIBLE);
                        }
                        if (item.getStart_at().equals("") || item.getStart_at().equals(null)) {
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
        Intent intent = new Intent(ListEventActivity.this, SyncronizationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                Intent intent = new Intent(ListEventActivity.this, SyncronizationActivity.class);
                startActivity(intent);
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

        return false;
    }
}
