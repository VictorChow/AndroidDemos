package demos.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 16/2/15.
 */
public class ContactModule {
    private String name;
    private List<String> phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void addPhone(String phone) {
        if (phones == null) {
            phones = new ArrayList<>();
        }
        phones.add(phone);
    }

}
