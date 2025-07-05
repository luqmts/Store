package com.luq.storevs.valueobjects;

import java.util.InputMismatchException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Cnpj {
    private String cnpj;
    private String cnpjraw;
    
    @JsonCreator
    public Cnpj(String stringcnpj) {
        setcnpj(stringcnpj);
    }

    @JsonValue
    public String getValue() {
        return cnpj;
    }

    public void setcnpj(String stringcnpj){
        if (validatecnpj(stringcnpj)) {
            this.cnpjraw = stringcnpj.replace(".", "")
                .replace("-", "")
                .replace("/", "");
            this.cnpj = stringcnpj;
        } else throw new IllegalArgumentException("Invalid cnpj format.");   
    }


    private boolean validatecnpj(String stringcnpj) {
        if (stringcnpj == null) throw new NullPointerException("cnpj must not be null!");

        if (stringcnpj.length() == 18) {
            stringcnpj = stringcnpj.replace(".", "")
                .replace("-", "")
                .replace("/", "");
        }

        if (
            stringcnpj.equals("00000000000000") || stringcnpj.equals("11111111111111") ||
            stringcnpj.equals("22222222222222") || stringcnpj.equals("33333333333333") ||
            stringcnpj.equals("44444444444444") || stringcnpj.equals("55555555555555") ||
            stringcnpj.equals("66666666666666") || stringcnpj.equals("77777777777777") ||
            stringcnpj.equals("88888888888888") || stringcnpj.equals("99999999999999") ||
            (stringcnpj.length() != 14)
        ) return false;

        char dig13, dig14;
        int sm, i, remainder, num, weight;

        try {
            sm = 0;
            weight = 2;
            for (i=11; i>=0; i--) {
                num = (int)(stringcnpj.charAt(i) - 48);
                sm = sm + (num * weight);
                weight = weight + 1;
                if (weight == 10) weight = 2;
            }

            remainder = sm % 11;
            if ((remainder == 0) || (remainder == 1)) dig13 = '0';
            else dig13 = (char)((11-remainder) + 48);

            sm = 0;
            weight = 2;
            for (i=12; i>=0; i--) {
                num = (int)(stringcnpj.charAt(i)- 48);
                sm = sm + (num * weight);
                weight = weight + 1;
                if (weight == 10) weight = 2;
            }

            remainder = sm % 11;
            if ((remainder == 0) || (remainder == 1)) dig14 = '0';
            else dig14 = (char)((11-remainder) + 48);


            if ((dig13 == stringcnpj.charAt(12)) && (dig14 == stringcnpj.charAt(13))) return true;
            else return false;
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getValue();
    }
}
