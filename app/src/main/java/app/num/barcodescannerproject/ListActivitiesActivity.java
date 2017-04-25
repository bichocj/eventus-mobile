package app.num.barcodescannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapters.ActivityAdapter;
import Adapters.EventAdapter;
import Objects.ActivityResponse;
import Objects.EventResponse;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 21/07/2016.
 */
public class ListActivitiesActivity extends AppCompatActivity {
    private String pkEvent;
    private String nameEvent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
        for (int i = 0; i < 3; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(tabListener));
        }
        Bundle extras= getIntent().getExtras();
        this.nameEvent="Emesh";
        if(extras != null){
            pkEvent=extras.getString("pkEvent");
            nameEvent=extras.getString("nameEvent");
            getSupportActionBar().setTitle(nameEvent);

        }
        setContentView(R.layout.v_activities_list);
        showActivities();
    }

    private void showActivities() {
        SqlliteConsulter MDB= new SqlliteConsulter(ListActivitiesActivity.this.getApplicationContext());
        ArrayList<ActivityResponse> activityResponses =MDB.getActivities(pkEvent);
        final ListView lv= (ListView)findViewById(R.id.Activities_listView);
        if(activityResponses!=null) {
            ActivityAdapter adapter= new ActivityAdapter(ListActivitiesActivity.this,0,activityResponses);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityResponse item = (ActivityResponse) lv.getItemAtPosition(position);
                Intent intent = new Intent(ListActivitiesActivity.this, ListAsistansActivity.class);
                intent.putExtra("pkActivity",item.getPk());
                intent.putExtra("pkEvent",pkEvent);
                intent.putExtra("nameActivity",item.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_activity_list,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.reload:
                setContentView(R.layout.v_activities_list);
                showActivities();
                Toast.makeText(ListActivitiesActivity.this,"Refrescado",Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
