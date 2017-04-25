package Objects;

/**
 * Created by Administrador on 24/04/2017.
 */
public class SyncDbObj {
    String date;
    String pk;
    String success;

    public SyncDbObj(String pk, String date, String success) {
        this.date = date;
        this.pk = pk;
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
