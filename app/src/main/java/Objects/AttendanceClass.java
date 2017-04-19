package Objects;

/**
 * Created by Administrador on 17/03/2017.
 */
public class AttendanceClass {
    String activity;
    String register;

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
