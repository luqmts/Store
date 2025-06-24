package valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mail {
    private String mail;
    
    public Mail(String stringMail) {
        setMail(stringMail);
    }

    public void setMail(String stringMail){
        if (validateMail(stringMail)) this.mail = stringMail;
        else throw new IllegalArgumentException("Invalid mail format.");   
    }

    public String getMail(){
        return this.mail;
    }

    public boolean validateMail(String stringMail){
        if (stringMail == null) throw new NullPointerException("Mail must not be null!");

        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(stringMail);

        return matcher.matches(); 
    }

    @Override
    public String toString() {
        return getMail();
    }
}
