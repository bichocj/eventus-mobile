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
import java.util.List;

import Objects.ActivityResponse;
import Objects.AttendanceClass;
import Objects.EventResponse;
import Objects.RegisterResponse;
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
            "(pk TEXT PRIMARY KEY,name TEXT,domain TEXT,description TEXT, slug TEXT, slogan TEXT, start_at TEXT," +
            " end_at TEXT,owner TEXT,is_active TEXT,event_type TEXT,city TEXT, localitation TEXT,address TEXT)";
    private static final String TABLA_ACTIVITIES="CREATE TABLE activity" +
            "(pk TEXT PRIMARY KEY,name TEXT, start_at TEXT,end_at TEXT, address TEXT,capacity TEXT,event_type TEXT,max_per_user TEXT,event TEXT,speaker TEXT,avaliable TEXT,registered_number TEXT, attendees TEXT, institution TEXT)";
    private static final String TABLA_ATTENDANCE="CREATE TABLE attendance" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT,activity TEXT,register TEXT, server INTEGER DEFAULT 0)";
    private static final String TABLA_REGISTERS="CREATE TABLE register" +
            "(pk TEXT PRIMARY KEY,activity TEXT, ticket TEXT,first_name TEXT, last_name TEXT,have_attendance TEXT,pk_register TEXT)";
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
        onCreate(db);
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
            String insertExec= "INSERT INTO register(pk,activity , ticket ,first_name , last_name ,have_attendance,pk_register )" +
                    "VALUES(?1,?2,?3,?4,?5,?6,?7)";
            SQLiteStatement stm= db.compileStatement(insertExec);
            stm.bindString(1,""+registerResponse.getPk());
            stm.bindString(2,""+registerResponse.getActivity());
            stm.bindString(3,""+registerResponse.getRegister().getTicket().getName());
            stm.bindString(4,""+registerResponse.getRegister().getPerson().getFirst_name());
            stm.bindString(5,""+registerResponse.getRegister().getPerson().getLast_name());
            stm.bindString(6,""+registerResponse.getHave_attendance());
            stm.bindString(7,""+registerResponse.getRegister().getPk());
            stm.execute();
        }

        return true;
    }
    public boolean AddActivity(List<ActivityResponse> activityResponses)
    {
        SQLiteDatabase db= getWritableDatabase();
        for (ActivityResponse activityResponse:activityResponses) {
            String insertExec= "INSERT INTO activity(pk,name ,start_at ,end_at ,address ,capacity,event_type,max_per_user,event ,speaker ,avaliable ,registered_number ,attendees ,institution )" +
                    "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14)";
            SQLiteStatement stm= db.compileStatement(insertExec);
            stm.bindString(1,""+activityResponse.getPk());
            stm.bindString(2,""+activityResponse.getName());
            stm.bindString(3,""+activityResponse.getStart_at());
            stm.bindString(4,""+activityResponse.getEnd_at());
            stm.bindString(5,""+activityResponse.getAddress());
            stm.bindString(6,""+activityResponse.getCapacity());
            stm.bindString(7,""+activityResponse.getType().getLowercased());
            stm.bindString(8,""+activityResponse.getType().getMax_per_user());
            stm.bindString(9,""+activityResponse.getEvent());
            stm.bindString(10,""+activityResponse.getSpeaker());
            stm.bindString(11,""+activityResponse.getAvaliable());
            stm.bindString(12,""+activityResponse.getRegistered_number());
            stm.bindString(13,""+activityResponse.getAttendees());
            stm.bindString(14,""+activityResponse.getInstitution());
            stm.execute();
        }

        return true;
    }
    public boolean AddEvent(EventResponse eventResponse)
    {
        SQLiteDatabase db= getWritableDatabase();
        /*for (EventResponse eventResponse1:eventResponse) {*/
            String insertExec= "INSERT INTO event(pk ,name, domain,description,slug,slogan,start_at,end_at,owner,is_active,event_type,city,localitation,address) " +
                                "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14)";
            SQLiteStatement stm= db.compileStatement(insertExec);
            stm.bindString(1,""+eventResponse.getPk());
            stm.bindString(2,""+eventResponse.getName());
            stm.bindString(3,""+eventResponse.getDomain());
            stm.bindString(4,""+eventResponse.getDescription());
            stm.bindString(5,""+eventResponse.getSlug());
            stm.bindString(6,""+eventResponse.getSlogan());
            stm.bindString(7,""+eventResponse.getStart_at());
            stm.bindString(8,""+eventResponse.getEnd_at());
            stm.bindString(9,""+eventResponse.getOwner());
            stm.bindString(10,""+eventResponse.getIs_active());
            stm.bindString(11,""+eventResponse.getEvent_type());
            stm.bindString(12,""+eventResponse.getCity());
            stm.bindString(13,""+eventResponse.getLocalitation());
            stm.bindString(14,""+eventResponse.getAddress());
            stm.execute();


        return true;
    }
    public ArrayList<RegisterResponse> showRegister(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<RegisterResponse> registerResponses= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM register",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    RegisterResponse registerResponse = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                    registerResponses.add(registerResponse);

                }while(c.moveToNext());
                return registerResponses;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }
    public ArrayList<ActivityResponse> showActivity(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<ActivityResponse> acttivities= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM activity",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    ActivityResponse activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13));
                    acttivities.add(activity);

                }while(c.moveToNext());
                return acttivities;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }

    public ArrayList<EventResponse> showEvent(){
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<EventResponse> events= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM event",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    EventResponse event = new EventResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13));
                    events.add(event);

                }while(c.moveToNext());
                return events;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }

    public ArrayList<ActivityResponse> getActivities(String pk) {
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<ActivityResponse> activityResponses= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM activity WHERE event= ? ",new String[]{pk});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    ActivityResponse activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13));
                    activityResponses.add(activity);

                }while(c.moveToNext());
                return activityResponses;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;

    }

    public ActivityResponse getActivity(String pkActivity) {
        SQLiteDatabase db= this.getReadableDatabase();
        ActivityResponse activity;
        try{
            Cursor c=db.rawQuery("SELECT * FROM activity WHERE pk= ? ",new String[]{pkActivity});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    activity = new ActivityResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13));

                }while(c.moveToNext());
                return activity;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }

    public ArrayList<RegisterResponse> getRegisters(String pkEvent, String pkActivity) {
        SQLiteDatabase db= this.getReadableDatabase();
        ArrayList<RegisterResponse> activityResponses= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM register WHERE activity= ? ",new String[]{pkActivity});
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    RegisterResponse activity = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                    activityResponses.add(activity);

                }while(c.moveToNext());
                return activityResponses;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }

    public int getRegisterQuantity(String pkActivity){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<RegisterResponse> registers= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM register WHERE activity= ? ",new String[]{pkActivity} );
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }
        return -1;

    }

    public int getRegisterQuantityAll(){
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            Cursor c=db.rawQuery("SELECT * FROM register ",null);
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }
        return -1;

    }

    public int getAttendanceQuantity(String pkActivity){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<AttendanceClass> attendances= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM attendance WHERE activity= ? ",new String[]{pkActivity});
            if (c.getCount() > 0){
                return c.getCount();
            }

        }catch (SQLiteException e){
            return -1;
        }
        return -1;

    }

    public ArrayList<AttendanceClass> getAttendance(){
        SQLiteDatabase db= this.getWritableDatabase();
        ArrayList<AttendanceClass> attendances= new ArrayList<>();
        try{
            Cursor c=db.rawQuery("SELECT * FROM attendance WHERE server = 0",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    AttendanceClass attendance = new AttendanceClass(c.getString(1), c.getString(2));
                    attendances.add(attendance);

                }while(c.moveToNext());
                return attendances;
            }
            if (c.getCount()==0)
                return attendances;

        }catch (SQLiteException e){
            return null;
        }
        return null;

    }

    public void AddAssitanceInSync(String Activity, String Register){
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            Cursor c=db.rawQuery("SELECT * FROM attendance WHERE activity= ? and register=? ",new String[]{Activity,Register});
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
        }


    }

    public String TakeAssistance(String scanContent,String pkActivity) {
        SQLiteDatabase db= this.getWritableDatabase();
        try{
            Cursor c=db.rawQuery("SELECT * FROM register WHERE activity= ? and pk_register=? ",new String[]{pkActivity,scanContent});
            RegisterResponse register;
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    register = new RegisterResponse(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));
                }while(c.moveToNext());
                try{
                    ContentValues valores = new ContentValues();
                    valores.put("have_attendance","true");
                    int cout=db.update("register", valores, "activity= ? and pk_register=? ",new String[]{pkActivity,scanContent});

                    if (cout < 0){
                        return null;
                    }
                    else{
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
                            stm.bindString(3,""+0);
                            stm.execute();
                        }
                        String insertExec= "INSERT INTO attendance(activity,register,server)" +
                                "VALUES(?1,?2,?3)";
                        SQLiteStatement stm= db.compileStatement(insertExec);
                        stm.bindString(1,""+pkActivity);
                        stm.bindString(2,""+scanContent);
                        stm.bindString(3,""+0);
                        stm.execute();

                    }

                }catch (SQLiteException e){
                    return null;
                }
                return register.getRegister().getPerson().getFirst_name()+' '+register.getRegister().getPerson().getLast_name();
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;
    }

    public User isLoggedUser() {
        SQLiteDatabase db= this.getReadableDatabase();
        User activity;
        try{
            Cursor c=db.rawQuery("SELECT * FROM user",null);
            if (c.getCount() > 0){
                c.moveToFirst();
                do{
                    activity = new User(c.getString(0), c.getString(1), c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6));

                }while(c.moveToNext());
                return activity;
            }

        }catch (SQLiteException e){
            return null;
        }
        return null;

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
