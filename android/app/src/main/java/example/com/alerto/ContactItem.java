package example.com.alerto;

/**
 * Created by adarsh on 11/12/15.
 */
public class ContactItem {
    protected String name;
    protected String phoneno;
    protected String Id;

    public ContactItem(String dname, String phoneno, String Id) {
        this.name = dname;
        this.phoneno = phoneno;
        this.Id = Id;
    }

    public String getItemName(){
        return name;
    }

    public String getPhoneNo(){
        return phoneno;
    }

    public String getId(){
        return Id;
    }
}

