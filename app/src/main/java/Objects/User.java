package Objects;

/**
 * Created by Administrador on 21/07/2016.
 */
public class User {
    String username;
    String password;
    String pk;
    String logueado;
    String sync_local;
    String sync_web;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String token;

    public User(String pk,String username, String password,  String logueado, String sync_local, String sync_web, String token) {
        this.username = username;
        this.password = password;
        this.pk = pk;
        this.logueado = logueado;
        this.sync_local = sync_local;
        this.sync_web = sync_web;
        this.token=token;
    }

    public User() {
    }

    public String getPk() {

        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getLogueado() {
        return logueado;
    }

    public void setLogueado(String logueado) {
        this.logueado = logueado;
    }

    public String getSync_local() {
        return sync_local;
    }

    public void setSync_local(String sync_local) {
        this.sync_local = sync_local;
    }

    public String getSync_web() {
        return sync_web;
    }

    public void setSync_web(String sync_web) {
        this.sync_web = sync_web;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
