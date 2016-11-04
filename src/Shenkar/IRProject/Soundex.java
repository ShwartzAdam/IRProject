package Shenkar.IRProject;

public class Soundex {
    private String _code;

    public Soundex(String code) {
        //System.out.print(code + " - ");
        char license = code.charAt(0);
        code = removeHW(code);
        code = removeZero(code);
        code = convertToNumber(code);
        code = removeZero(code);
        code = checkDouble(code);
        code = removeZero(code);
        code = shapeCode(code, license);
        //System.out.println(code);
        this._code = code;
    }

    public String getCode() {
        return _code;
    }
    public void setCode(String code) {
        this._code = code;
    }


    @Override
    public String toString() {
        return "Soundex [code=" + _code + "]";
    }

    String removeZero(String str)
    {
        int i = 0;
        while (i < str.length())
        {
            if (str.charAt(i) == '0')
            {

                str = str.substring(0, i) + str.substring(i + 1, str.length());
                i = 0;
            }
            else
            {
                i++;
            }
        }
        return str;
    }

    String removeHW(String str)
    {
        str = str.substring(1);
        str = str.replaceAll("[WwHh]", "0");
        return str;
    }

    String convertToNumber(String str)
    {
        str = str.toLowerCase();
        str = str.replaceAll("[aehiouwy]", "0");
        str = str.replaceAll("[bpvf]", "1");
        str = str.replaceAll("[cskgjqxz]", "2");
        str = str.replaceAll("[dt]", "3");
        str = str.replaceAll("[l]", "4");
        str = str.replaceAll("[mn]", "5");
        str = str.replaceAll("[r]", "6");
        return str;
    }

    String checkDouble(String str)
    {
        int i = 0, j = 1;
        char letter_i = '0', letter_j = '0';
        while (i < str.length() - 1)
        {
            letter_i = str.charAt(i);
            letter_j = str.charAt(j);
            if ( (letter_i == letter_j))
            {
                str = str.substring(0, i) + str.substring(i + 1, str.length());
                j = i + 1;
            }
            else
            {
                j++;
                i++;
            }
        }
        return str;
    }



    String shapeCode(String str, char license)
    {
        license = Character.toUpperCase(license);
        str = license + str.substring(0,str.length());
        while(str.length() < 4)
        {
            str = str + '0';
        }
        if(str.length() > 3)
        {
            str = str.substring(0,4);
        }
        return str;
    }



}
