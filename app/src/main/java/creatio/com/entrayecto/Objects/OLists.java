package creatio.com.entrayecto.Objects;

/**
 * Created by Layge on 19/12/2017.
 */

public class OLists {
    String name, create_date, quantity, image, ID_branch, name_branch, finish_date, menu_relation, web_relation, status;

    public OLists(String name, String create_date, String quantity, String image, String ID_branch, String name_branch, String finish_date, String menu_relation, String web_relation, String status) {
        this.name = name;
        this.create_date = create_date;
        this.quantity = quantity;
        this.image = image;
        this.ID_branch = ID_branch;
        this.name_branch = name_branch;
        this.finish_date = finish_date;
        this.menu_relation = menu_relation;
        this.web_relation = web_relation;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getID_branch() {
        return ID_branch;
    }

    public void setID_branch(String ID_branch) {
        this.ID_branch = ID_branch;
    }

    public String getName_branch() {
        return name_branch;
    }

    public void setName_branch(String name_branch) {
        this.name_branch = name_branch;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getMenu_relation() {
        return menu_relation;
    }

    public void setMenu_relation(String menu_relation) {
        this.menu_relation = menu_relation;
    }

    public String getWeb_relation() {
        return web_relation;
    }

    public void setWeb_relation(String web_relation) {
        this.web_relation = web_relation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
