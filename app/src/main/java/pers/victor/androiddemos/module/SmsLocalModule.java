package pers.victor.androiddemos.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 16/2/14.
 */
public class SmsLocalModule {
    private String number;
    private String name;
    private List<String> types;
    private List<String> bodies;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBodyAndType(String body, String type) {
        if (bodies == null) {
            bodies = new ArrayList<>();
        }
        if (types == null) {
            types = new ArrayList<>();
        }
        bodies.add(body);
        types.add(type);
    }


    public List<String> getBodies() {
        return bodies;
    }

    public List<String> getTypes() {
        return types;
    }
}
