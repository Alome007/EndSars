package end.sars.now.Helpers;

public class politiciansHelper {
    String id;
    String name;
    String state;
    String phoneNo;

    public politiciansHelper() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public politiciansHelper(String id, String name, String state, String phoneNo, String email) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    String email;
}
