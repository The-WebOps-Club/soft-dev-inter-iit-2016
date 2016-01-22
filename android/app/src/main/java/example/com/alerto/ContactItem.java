package example.com.alerto;

/**
 * Created by adarsh on 11/12/15.
 */
public class ContactItem {
    protected String name;
    protected String phoneno;

    public ContactItem(String dname, String phoneno) {
        this.name = dname;
        this.phoneno = phoneno;
    }

    public String getItemName(){
        return name;
    }

    public String getPhoneNo(){
        return phoneno;
    }
}

