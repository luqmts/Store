package valueobjects;

import java.util.InputMismatchException;

public class CNPJ {
    private String CNPJ;
    private String CNPJraw;
    
    public CNPJ(String stringCNPJ) {
        setCNPJ(stringCNPJ);
    }

    public void setCNPJ(String stringCNPJ){
        if (validateCNPJ(stringCNPJ)) {
            this.CNPJraw = stringCNPJ.replace(".", "")
                .replace("-", "")
                .replace("/", "");
            this.CNPJ = stringCNPJ;
        } else throw new IllegalArgumentException("Invalid CNPJ format.");   
    }

    public String getCNPJ(){
        return this.CNPJ;
    }

    public String getCNPJraw(){
        return this.CNPJraw;
    }

    private boolean validateCNPJ(String stringCNPJ) {
        if (stringCNPJ == null) throw new NullPointerException("CNPJ must not be null!");

        if (stringCNPJ.length() == 18) {
            stringCNPJ = stringCNPJ.replace(".", "")
                .replace("-", "")
                .replace("/", "");
        }

        if (
            stringCNPJ.equals("00000000000000") || stringCNPJ.equals("11111111111111") ||
            stringCNPJ.equals("22222222222222") || stringCNPJ.equals("33333333333333") ||
            stringCNPJ.equals("44444444444444") || stringCNPJ.equals("55555555555555") ||
            stringCNPJ.equals("66666666666666") || stringCNPJ.equals("77777777777777") ||
            stringCNPJ.equals("88888888888888") || stringCNPJ.equals("99999999999999") ||
            (stringCNPJ.length() != 14)
        ) return false;

        char dig13, dig14;
        int sm, i, remainder, num, weight;

        try {
            sm = 0;
            weight = 2;
            for (i=11; i>=0; i--) {
                num = (int)(stringCNPJ.charAt(i) - 48);
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
                num = (int)(stringCNPJ.charAt(i)- 48);
                sm = sm + (num * weight);
                weight = weight + 1;
                if (weight == 10) weight = 2;
            }

            remainder = sm % 11;
            if ((remainder == 0) || (remainder == 1)) dig14 = '0';
            else dig14 = (char)((11-remainder) + 48);


            if ((dig13 == stringCNPJ.charAt(12)) && (dig14 == stringCNPJ.charAt(13))) return true;
            else return false;
        } catch (InputMismatchException erro) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getCNPJ();
    }
}
