package creatio.com.entrayecto.Objects;

/**
 * Created by Layge on 21/12/2017.
 */

public class OItemMenu {
    String ID,name,price,create_date,ID_branch,image,description;

    public OItemMenu(String ID,String name, String price, String create_date, String ID_branch, String image, String description) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.create_date = create_date;
        this.ID_branch = ID_branch;
        this.image = image;
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getID_branch() {
        return ID_branch;
    }

    public void setID_branch(String ID_branch) {
        this.ID_branch = ID_branch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
