package app.num.barcodescannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import Adapters.ActivityAdapter;
import Objects.ActivityResponse;
import dbapp.SqlliteConsulter;

/**
 * Created by Administrador on 21/07/2016.
 */
public class ListActivitiesActivity extends AppCompatActivity {
    private String pkEvent;
    private String nameEvent;
    SqlliteConsulter MDB;
    ArrayList<ActivityResponse> activityResponses;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MDB=new SqlliteConsulter(ListActivitiesActivity.this.getApplicationContext());
        final ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras= getIntent().getExtras();
        this.nameEvent="Emesh";
        if(extras != null){
            pkEvent=extras.getString("pkEvent");
            nameEvent=extras.getString("nameEvent");
            getSupportActionBar().setTitle(nameEvent);

        }
        setContentView(R.layout.v_activities_list);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                SimpleDateFormat formatter_to_show = new SimpleDateFormat("EEE dd");
                ArrayList<ActivityResponse> activityResponses_new=new ArrayList<>();
                try {
                    Date date_tab=formatter_to_show.parse((String) tab.getText());
                    for(ActivityResponse activityResponse:activityResponses){
                        Date date_activity=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(activityResponse.getStarts_at());
                        String string_date_activity=formatter_to_show.format(date_activity);
                        date_activity=formatter_to_show.parse(string_date_activity);
                        if(date_activity.getTime()== date_tab.getTime()){
                                activityResponses_new.add(activityResponse);
                        }
                    }
                    showActivities(activityResponses_new);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        activityResponses =MDB.getActivitiesByEventPk(pkEvent);
        ArrayList<Date> dates=new ArrayList<>();
        Boolean flag=true;
        if (activityResponses == null){
            Toast.makeText(ListActivitiesActivity.this,"No tiene actividades",Toast.LENGTH_SHORT).show();
            ListActivitiesActivity.this.finish();
        }else{
            for (ActivityResponse activityResponse: activityResponses){
                try {
                    String new_date="";
                    Date date_Compare=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(activityResponse.getStarts_at());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    new_date= df.format(date_Compare);
                    date_Compare=df.parse(new_date);
                    if (dates.size() == 0){
                        dates.add(date_Compare);
                    }
                    for(Date date : dates) {
                        if(date_Compare.getTime() == date.getTime()){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        dates.add(date_Compare);
                    }
                    else{
                        flag=true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            Collections.sort(dates, new Comparator<Date>() {
                @Override
                public int compare(Date lhs, Date rhs) {
                    if (lhs.getTime() < rhs.getTime())
                        return -1;
                    else if (lhs.getTime() == rhs.getTime())
                        return 0;
                    else
                        return 1;
                }
            });
            for (Date date: dates) {
                SimpleDateFormat formatter_to_show = new SimpleDateFormat("EEE dd");
                String to_show=formatter_to_show.format(date);
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(to_show)
                                .setTabListener(tabListener));
            }
        }


        //showActivities();
    }

    private void showActivities(ArrayList<ActivityResponse> activities_new) {
        final ListView lv= (ListView)findViewById(R.id.Activities_listView);
        if(activities_new!=null) {
            ActivityAdapter adapter= new ActivityAdapter(ListActivitiesActivity.this,0,activities_new);
            lv.setAdapter(adapter);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final LayoutInflater inflater = getLayoutInflater();
                ImageView info_view_activity=(ImageView) view.findViewById(R.id.Info_activity);
                info_view_activity.setClickable(true);
                info_view_activity.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListActivitiesActivity.this,"hola",Toast.LENGTH_SHORT).show();
                    }
                });
                ActivityResponse item = (ActivityResponse) lv.getItemAtPosition(position);
                Intent intent = new Intent(ListActivitiesActivity.this, ListAsistansActivity.class);
                intent.putExtra("pkActivity",item.getPk());
                intent.putExtra("pkEvent",pkEvent);
                intent.putExtra("nameActivity",item.getName());
                intent.putExtra("nameEvent",nameEvent);
                startActivity(intent);
                ListActivitiesActivity.this.finish();
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
        /*switch (item.getItemId()) {
            case R.id.back:
                ListActivitiesActivity.this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        if (item.getItemId() == android.R.id.home ) {
            ListActivitiesActivity.this.finish();
            Intent intent=new Intent(ListActivitiesActivity.this,ListEventActivity.class);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
