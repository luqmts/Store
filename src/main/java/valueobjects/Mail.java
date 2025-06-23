package valueobjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mail {
    private String mail;
    
    public Mail(String stringMail) {
        setMail(stringMail);
    }

    public void setMail(String stringMail){
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(stringMail);

        if (matcher.matches()) this.mail = stringMail;
        else throw new Error("Invalid mail format.");   
    }

    public String getMail(){
        return this.mail;
    }
}
