package other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoop on 2015/6/30 0030.
 */
public class Employee {
    private String firstName;
    private String lastName;
    private boolean sex = true;
    private List<Employee> lists = new ArrayList<>(11);
    private Map<String,Address> addresses = new HashMap<>();
    public Employee(){
        lists.add(this);
        addresses.put("home",new Address("hubei"));
    }

    public Employee(String firstName) {
        lists.add(this);
        addresses.put("home", new Address("hubei"));
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Employee getLists(int index){
        return lists.get(index);
    }
    public void setLists(int index, Employee subordinate){
        lists.set(index,subordinate);
    }
    public Address getAddresses(String type){
        return addresses.get(type);
    }
    public void setAddresses(String type, Address address){
        addresses.put(type,address);
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                '}';
    }
}
