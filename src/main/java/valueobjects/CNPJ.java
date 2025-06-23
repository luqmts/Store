package valueobjects;

import java.util.InputMismatchException;

public class CNPJ {
    private String CNPJ;
    private String CNPJraw;
    
    public CNPJ(String stringCNPJ) {
        setCNPJ(stringCNPJ);
    }

    public void setCNPJ(String stringCNPJ){
        if (stringCNPJ.length() == 18) {
            stringCNPJ = stringCNPJ.replace(".", "")
                .replace("-", "")
                .replace("/", "");
        }

        if (validateCNPJ(stringCNPJ)) {
            this.CNPJraw = stringCNPJ;
            this.CNPJ = String.format(
                "%s.%s.%s/%s-%s",
                stringCNPJ.substring(0, 2), stringCNPJ.substring(2, 5),
                stringCNPJ.substring(5, 8), stringCNPJ.substring(8, 12), stringCNPJ.substring(12, 14)
            );
        } else throw new Error("Invalid CNPJ format.");   
    }

    public String getCNPJ(){
        return this.CNPJ;
    }

    public String getCNPJraw(){
        return this.CNPJraw;
    }

    private boolean validateCNPJ(String stringCNPJ) {
        if (
            CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
            CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
            CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
            CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
            CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
            (CNPJ.length() != 14)
        ) return false;

        char dig13, dig14;
        int sm, i, remainder, num, weight;

        try {
            sm = 0;
            weight = 2;
            for (i=11; i>=0; i--) {
                num = (int)(CNPJ.charAt(i) - 48);
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
                num = (int)(CNPJ.charAt(i)- 48);
                sm = sm + (num * weight);
                weight = weight + 1;
                if (weight == 10) weight = 2;
            }

            remainder = sm % 11;
            if ((remainder == 0) || (remainder == 1)) dig14 = '0';
            else dig14 = (char)((11-remainder) + 48);


            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13))) return true;
            else return false;
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}
