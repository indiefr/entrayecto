package creatio.com.entrayecto.Objects;

/**
 * Created by Layge on 21/12/2017.
 */

public class OMenu {
    String ID_categorie,name,description,image,create_date;

    public OMenu(String ID_categorie, String name, String description, String image, String create_date) {
        this.ID_categorie = ID_categorie;
        this.name = name;
        this.description = description;
        this.image = image;
        this.create_date = create_date;
    }

    public String getID_categorie() {
        return ID_categorie;
    }

    public void setID_categorie(String ID_categorie) {
        this.ID_categorie = ID_categorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
