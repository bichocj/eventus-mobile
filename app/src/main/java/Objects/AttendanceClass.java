package Objects;

/**
 * Created by Administrador on 17/03/2017.
 */
public class AttendanceClass {
    String pk;
    String activity;
    String register;

    public AttendanceClass(String pk, String activity, String register) {
        this.pk = pk;
        this.activity = activity;
        this.register = register;
    }

    public String getPk() {

        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public AttendanceClass(String activity, String register){
        this.activity=activity;
        this.register=register;
    }
}
