package creatio.com.entrayecto.Objects;

/**
 * Created by Layge on 21/12/2017.
 */

public class OContact {
    String name, name_branch, ID_branch, location, address, tel, email, service_out, horario;


    public OContact(String name, String name_branch, String ID_branch, String location, String address, String tel, String email, String service_out, String horario) {

        this.name = name;
        this.name_branch = name_branch;
        this.ID_branch = ID_branch;
        this.location = location;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.service_out = service_out;
        this.horario = horario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_branch() {
        return name_branch;
    }

    public void setName_branch(String name_branch) {
        this.name_branch = name_branch;
    }

    public String getID_branch() {
        return ID_branch;
    }

    public void setID_branch(String ID_branch) {
        this.ID_branch = ID_branch;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService_out() {
        return service_out;
    }

    public void setService_out(String service_out) {
        this.service_out = service_out;
    }
}
