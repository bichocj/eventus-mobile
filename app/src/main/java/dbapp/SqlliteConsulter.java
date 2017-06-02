package dbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Objects.ActivityResponse;
import Objects.AttendanceClass;
import Objects.EventResponse;
import Objects.RegisterResponse;
import Objects.SyncDbObj;
import Objects.User;

/**
 * Created by ALVARO on 23/05/2016.
 */
public class SqlliteConsulter extends SQLiteOpenHelper {
    private final Context context;
    private static final int VERSION_BASEDATOS=1;
    private static final String NOMBRE_BD="eventus.db";
    private static final String TABLA_USUARIO ="CREATE TABLE user" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,password TEXT,logueado TEXT,sync_local TEXT,sync_web TEXT,token TEXT)";
    private static final String TABLA_EVENT="CREATE TABLE event" +
            "(pk TEXT PRIMARY KEY,slug TEXT, name TEXT,domain TEXT, start_at TEXT, end_at TEXT, localitation TEXT,address TEXT)";
    private static final String TABLA_ACTIVITIES="CREATE TABLE activity" +
            "(pk TEXT PRIMARY KEY,name TEXT, starts_at TEXT,ends_at TEXT, event TEXT,info_about TEXT)";
    private static final String TABLA_SYNCS="CREATE TABLE syncronization" +
            "(pk TEXT PRIMARY KEY, date TEXT,success INTEGER DEFAULT 0)";

    private static final String TABLA_ATTENDANCE="CREATE TABLE attendance" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,activity TEXT,register TEXT, server INTEGER DEFAULT 0)";
    private static final String TABLA_REGISTERS="CREATE TABLE register" +
            "(pk TEXT PRIMARY KEY,first_name TEXT, last_name TEXT)";
    public SqlliteConsulter(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BASEDATOS);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_USUARIO);
        db.execSQL(TABLA_EVENT);
        db.execSQL(TABLA_ACTIVITIES);
        db.execSQL(TABLA_REGISTERS);
        db.execSQL(TABLA_ATTENDANCE);
        db.execSQL(TABLA_SYNCS);

    }
    public boolean logoutDB(){
        SQLiteDatabase db= getWritableDatabase();
        try {
            db.execSQL("DELETE FROM user");
            return true;
        }
        catch (SQLiteException e){
            return true;
        }

    }
    public boolean deleteAllDB(){
        SQLiteDatabase db= getWritableDatabase();
        try {
            db.execSQL("DELETE FROM event");
            db.execSQL("DELETE FROM activity");
            db.execSQL("DELETE FROM register");
            db.execSQL("DELETE FROM attendance");
            db.execSQL("DELETE FROM syncronization");
            onCreate(db);
            return true;
        }
        catch (SQLiteException e){
            return true;
        }

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_EVENT);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_REGISTERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLA_SYNCS);
        onCreate(db);
    }
    public boolean insertActualization(Boolean success){
        SQLiteDatabase db= getWritableDatabase();
        String insertExec= "INSERT INTO syncronization(date ,success)" +
                "VALUES(?1,?2)";
        SQLiteStatement stm= db.compileStatement(insertExec);
        stm.bindString(1,""+new Date());
        if (success){
            stm.bindString(2,""+0);
        }
        else{
            stm.bindString(2,""+1);
        }
        stm.execute();

        return true;
    }
    public int get_count_sync(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c=null;
        try {
            c= db.rawQuery("SELECT * FROM syncronization", null);
            return c.getCount();
        }
        catch (SQLiteException e){
            return -1;
        }finally {
            if(c !=null)
            {
                c.close();
            }
        }

    }
    public SyncDbObj get_last_sync_success(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM syncronization where success = 0 ",null);
            if (c.getCount() > 0){
                SyncDbObj syncDbObj;
                c.moveToLast();
                syncDbObj= new SyncDbObj(c.getString(0), c.getString(1), c.getString(2));
                return syncDbObj ;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;

    }
    public SyncDbObj get_last_sync(){
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM syncronization ORDER BY date DESC LIMIT 1",null);
            if (c.getCount() > 0){
                SyncDbObj syncDbObj;
                c.moveToFirst();
                do{
                    syncDbObj= new SyncDbObj(c.getString(0), c.getString(1), c.getString(2));

                }while(c.moveToNext());
                return syncDbObj ;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;

    }
    public boolean insertUser(User user){
        SQLiteDatabase db= getWritableDatabase();
        String insertExec= "INSERT INTO user(username ,password ,logueado ,sync_local ,sync_web, token )" +
                    "VALUES(?1,?2,?3,?4,?5,?6)";
            SQLiteStatement stm= db.compileStatement(insertExec);
            stm.bindString(1,""+user.getUsername());
            stm.bindString(2,""+user.getPassword());
            stm.bindString(3,""+user.getLogueado());
            stm.bindString(4,""+user.getSync_local());
            stm.bindString(5,""+user.getSync_web());
            stm.bindString(6,""+user.getToken());
            stm.execute();

        return true;
    }
    public boolean AddRegister(List<RegisterResponse> registerResponses)
    {
        SQLiteDatabase db= getWritableDatabase();
        for (RegisterResponse registerResponse:registerResponses) {
            try {
                String insertExec = "INSERT INTO register(pk,first_name , last_name)" +
                        "VALUES(?1,?2,?3)";
                SQLiteStatement stm = db.compileStatement(insertExec);
                stm.bindString(1, "" + registerResponse.getPk());
                stm.bindString(2, "" + registerResponse.getFirst_name());
                stm.bindString(3, "" + registerResponse.getLast_name());
                stm.execute();
            }catch (Exception e){
                try{
                    ContentValues valores = new ContentValues();
                    valores.put("first_name",registerResponse.getFirst_name());
                    valores.put("last_name",registerResponse.getLast_name());
                    int c=db.update("register", valores, "pk="+registerResponse.getPk(),null);

                }catch (SQLiteException ee){

                }

            }
        }
        return true;

    }
    public boolean AddActivity(List<ActivityResponse> activityResponses)
    {
        SQLiteDatabase db= getWritableDatabase();

        for (ActivityResponse activityResponse : activityResponses) {
            String insertExec = "INSERT INTO activity(pk,name ,starts_at ,ends_at ,event ,info_about)" +
                    "VALUES(?1,?2,?3,?4,?5,?6)";
            SQLiteStatement stm = db.compileStatement(insertExec);
            try {
                stm.bindString(1, "" + activityResponse.getPk());
                stm.bindString(2, "" + activityResponse.getName());
                stm.bindString(3, "" + activityResponse.getStarts_at());
                stm.bindString(4, "" + activityResponse.getEnds_at());
                stm.bindString(5, "" + activityResponse.getEvent());
                stm.bindString(6, "" + activityResponse.getInfo_about());
                stm.execute();
                if (activityResponse.getRegistered().size() > 0 && activityResponse.getRegistered() != null) {
                    for (String pk : activityResponse.getRegistered()) {
                        TakeAssistance(pk, activityResponse.getPk(), true);
                    }
                }

            }catch(Exception e){
                try {
                    ContentValues valores = new ContentValues();
                    valores.put("name", activityResponse.getName());
                    valores.put("starts_at", activityResponse.getStarts_at());
                    valores.put("ends_at", activityResponse.getEnds_at());
                    valores.put("event", activityResponse.getEvent());
                    valores.put("info_about", activityResponse.getInfo_about());
                    int c = db.update("activity", valores, "pk=" + activityResponse.getPk(), null);

                } catch (SQLiteException ee) {

                }
            }
        }
        return true;
    }
    public boolean AddEvent(List<EventResponse> eventResponses)
    {
        SQLiteDatabase db= getWritableDatabase();
        for (EventResponse eventResponse:eventResponses) {
            String insertExec = "INSERT INTO event(pk ,slug, name,domain,start_at,end_at,localitation,address) " +
                    "VALUES(?1,?2,?3,?4,?5,?6,?7,?8)";
            SQLiteStatement stm = db.compileStatement(insertExec);
            stm.bindString(1, "" + eventResponse.getPk());
            stm.bindString(2, "" + eventResponse.getSlug());
            stm.bindString(3, "" + eventResponse.getName());
            stm.bindString(4, "" + eventResponse.getDomain());
            stm.bindString(5, "" + eventResponse.getStart_at());
            stm.bindString(6, "" + eventResponse.getEnd_at());
            stm.bindString(7, "" + eventResponse.getLocalitation());
            stm.bindString(8, "" + eventResponse.getAddress());
            stm.execute();
        }

        return true;
    }
    public ArrayList<RegisterResponse> showRegister(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<RegisterResponse> registerResponses= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM register",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    RegisterResponse registerResponse = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2));
                    registerResponses.add(registerResponse);

                }while(c.moveToNext());
                return registerResponses;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }
    public ArrayList<ActivityResponse> showActivity(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<ActivityResponse> acttivities= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM activity",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    ActivityResponse activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5));
                    acttivities.add(activity);

                }while(c.moveToNext());
                return acttivities;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }

    public ArrayList<EventResponse> showEvent(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<EventResponse> events= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM event",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    EventResponse event = new EventResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7));
                    events.add(event);

                }while(c.moveToNext());
                return events;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }

    public ArrayList<ActivityResponse> getActivities(String pk) {
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<ActivityResponse> activityResponses= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM activity WHERE event= ? ",new String[]{pk});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    ActivityResponse activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5));
                    activityResponses.add(activity);

                }while(c.moveToNext());
                return activityResponses;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;

    }
    public EventResponse getEvent(String pkEvent) {
        SQLiteDatabase db= this.getReadableDatabase();
        EventResponse event;
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM event WHERE pk= ? ",new String[]{pkEvent});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    event = new EventResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7));

                }while(c.moveToNext());
                return event;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }

    public ActivityResponse getActivity(String pkActivity) {
        SQLiteDatabase db= this.getReadableDatabase();
        ActivityResponse activity;
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM activity WHERE pk= ? ",new String[]{pkActivity});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5));

                }while(c.moveToNext());
                return activity;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }

    public ArrayList<RegisterResponse> getRegisters(String pkEvent, String pkActivity) {
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<RegisterResponse> activityResponses= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM register ORDER BY first_name ASC",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    RegisterResponse activity = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2));
                    activityResponses.add(activity);

                }while(c.moveToNext());
                return activityResponses;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;
    }

    public int getRegisterQuantity(String pkActivity){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<RegisterResponse> registers= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM register",null);
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return -1;

    }

    public int getRegisterQuantityAll(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM register ",null);
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return -1;

    }

    public int getAttendanceQuantity(String pkActivity){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<AttendanceClass> attendances= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM attendance WHERE activity= ? ",new String[]{pkActivity});
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return -1;

    }
    public boolean have_attendance(String pk_Activity, String pk_register){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM attendance WHERE activity = ? AND register= ?",new String[]{pk_Activity,pk_register});
            if (c.getCount() > 0){
                return true;
            }
            return false;
        }catch (SQLiteException e){
            return false;
        }finally {
            if(c!=null){
                c.close();
            }
        }
    }
    public ArrayList<AttendanceClass> getAttendance(){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<AttendanceClass> attendances= new ArrayList<>();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM attendance WHERE server = 0",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    AttendanceClass attendance = new AttendanceClass(c.getString(0),c.getString(1), c.getString(2));
                    attendances.add(attendance);

                }while(c.moveToNext());
                return attendances;
            }
            if (c.getCount()==0)
                return attendances;

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;

    }

    public void AddAssitanceInSync(String Activity, String Register){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM attendance WHERE activity= ? and register=? ",new String[]{Activity,Register});
            if (c.getCount() == 0){
                String insertExec= "INSERT INTO attendance(activity,register,server)" +
                        "VALUES(?1,?2,?3)";
                SQLiteStatement stm= db.compileStatement(insertExec);
                stm.bindString(1,""+Activity);
                stm.bindString(2,""+Register);
                stm.bindString(3,""+1);
                stm.execute();
            }
        }
        catch (SQLiteException e){
            Log.d("AddAttendance",e.toString());
        }finally {
            if(c!=null){
                c.close();
            }
        }


    }
    public boolean  ExistAttendanceWithOutSync(){
        SQLiteDatabase db= this.getWritableDatabase();
        RegisterResponse register=new RegisterResponse();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM attendance WHERE server=0 ",null);
            if (c.getCount() > 0) {
                return true;
            }
            else
            {
                return false;
            }
        }catch (SQLiteException e){
            return false;
        }finally {
            if(c!=null){
                c.close();
            }
        }

    }
    public String TakeAssistance(String scanContent,String pkActivity,Boolean server) {
        SQLiteDatabase db= this.getWritableDatabase();
        RegisterResponse register=new RegisterResponse();
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM register WHERE pk=? ",new String[]{scanContent});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    register = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2));
                }while(c.moveToNext());
                try{
                    c=db.rawQuery("SELECT * FROM attendance WHERE activity= ? and register=? ",new String[]{pkActivity,scanContent});
                    if (c.getCount() > 0){
                        return "Ya tiene asistencia";
                    }
                }
                catch (SQLiteException e){
                    String insertExec= "INSERT INTO attendance(activity,register,server)" +
                            "VALUES(?1,?2,?3)";
                    SQLiteStatement stm= db.compileStatement(insertExec);
                    stm.bindString(1,""+pkActivity);
                    stm.bindString(2,""+scanContent);
                    if(server) {
                        stm.bindString(3,""+1);

                    }else {
                        stm.bindString(3,""+0);

                    }
                    stm.execute();
                    return register.getFirst_name()+' '+register.getLast_name();
                }
                String insertExec= "INSERT INTO attendance(activity,register,server)" +
                        "VALUES(?1,?2,?3)";
                SQLiteStatement stm= db.compileStatement(insertExec);
                stm.bindString(1,""+pkActivity);
                stm.bindString(2,""+scanContent);
                if(server) {
                    stm.bindString(3,""+1);

                }else {
                    stm.bindString(3,""+0);

                }
                stm.execute();
                return register.getFirst_name()+' '+register.getLast_name();

            }else {
                return null;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }

    }




    public User isLoggedUser() {
        SQLiteDatabase db= this.getReadableDatabase();
        User activity;
        Cursor c=null;
        try{
            c=db.rawQuery("SELECT * FROM user",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    activity = new User(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));

                }while(c.moveToNext());
                return activity;
            }

        }catch (SQLiteException e){
            return null;
        }finally {
            if(c!=null){
                c.close();
            }
        }
        return null;

    }
    public boolean updateAttendance(String attendance){
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            ContentValues valores = new ContentValues();
            valores.put("server",1);
            int c=db.update("attendance", valores, "_id="+attendance,null);

            if (c > 0){

                return true;
            }

        }catch (SQLiteException e){
            return false;
        }
        return false;
    }
    public boolean synclocalset(String aTrue) {
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            ContentValues valores = new ContentValues();
            valores.put("sync_local","true");
            int c=db.update("user", valores, "logueado= 'true' ",null);

            if (c > 0){

                return true;
            }

        }catch (SQLiteException e){
            return false;
        }
        return false;
    }

    public boolean syncWebset(String aTrue) {
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            ContentValues valores = new ContentValues();
            valores.put("sync_web","true");
            int c=db.update("user", valores, "logueado= 'true' ",null);
            if (c > 0){
                return true;
            }
        }catch (SQLiteException e){
            return false;
        }
        return false;
    }


}
