package valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Phone {
    private String phone;
    
    public Phone(String stringPhone){
        setPhone(stringPhone);
    }

    public void setPhone(String stringPhone){
        if (validatePhone(stringPhone)) this.phone = stringPhone;
        else throw new IllegalArgumentException("Invalid phone format.");   
    }

    public String getPhone(){
        return this.phone;
    }

    public boolean validatePhone(String stringPhone){
        if (stringPhone == null) throw new NullPointerException("Phone must not be null!");

        Pattern pattern = Pattern.compile("[0-9]{2}[0-9]{8,9}");
        Matcher matcher = pattern.matcher(stringPhone);

        return matcher.matches();
    }

    @Override
    public String toString() {
        return getPhone();
    }
}
