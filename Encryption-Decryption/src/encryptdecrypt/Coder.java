package encryptdecrypt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Coder implements Encode ,Decode {

    private String [] information;
    private String encOrDec = "enc";
    private String alg = "shift";
    private int key = 0;
    private  List<String> info;
    private Object[] infoArray;
    private String payload = "";
    private String in = null;
    private String out = null;
    private boolean dataFlag = false;
    private FileWriter fw = null;
    private char[] arrayOfChars;

    public Coder(String [] information) {
        this.information = information;
        this.info = Arrays.stream(information).collect(Collectors.toList());
        this.infoArray = Arrays.stream(information).toArray();
        start();
    }

    private void start(){
        if (!info.contains("enc"))
            encOrDec = "dec";
        if (!info.contains("shift"))
            alg = "unicode";

        for (int i = 0; i < infoArray.length; i++) {
            if (infoArray[i].equals("-key")) {
                key = Integer.parseInt((String) infoArray[i + 1]);
                break;
            }
        }

        for (int i = 0; i < infoArray.length; i++) {
            if (infoArray[i].equals("-data")) {
                payload = (String) infoArray[i + 1];
                dataFlag = true;
                break;
            }
        }

        if(!dataFlag) {
            for (int i = 0; i < infoArray.length; i++) {
                if (infoArray[i].equals("-in")) {
                    in = (String) infoArray[i + 1];
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(in));
                        payload = br.readLine();
                        br.close();
                    } catch (IOException e) {
                        System.out.println("Error");
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < infoArray.length; i++) {
            if (infoArray[i].equals("-out")) {
                out = (String) infoArray[i + 1];
                try {
                    fw = new FileWriter(out);
                }  catch (IOException e) {
                    System.out.println("Error out");
                }
                break;
            }
        }

        this.arrayOfChars = payload.toCharArray();

        if(encOrDec.equals("enc") && alg.equals("shift"))
            EncodeShift();
        else if(encOrDec.equals("enc") && alg.equals("unicode"))
            EncodeUnicode();
        else if (encOrDec.equals("dec") && alg.equals("shift"))
            DecodeShift();
        else
            DecodeUnicode();

        Printer();
    }


    @Override
    public void DecodeUnicode() {
        for (int i = 0; i < arrayOfChars.length; i++) {
            for (int j = 0; j < key; j++) {
                arrayOfChars[i]--;
            }
        }
    }

    @Override
    public void DecodeShift() {
        for (int i = 0; i < arrayOfChars.length; i++) {
            for (int j = 0; j < key; j++) {
                if (arrayOfChars[i] >= 'A' && arrayOfChars[i] <= 'Z') {
                    if (arrayOfChars[i] - 1 < 'A')
                        arrayOfChars[i] = 'Z';
                    else
                        arrayOfChars[i]--;
                }
                if (arrayOfChars[i] >= 'a' && arrayOfChars[i] <= 'z') {
                    if (arrayOfChars[i] - 1 < 'a')
                        arrayOfChars[i] = 'z';
                    else
                        arrayOfChars[i]--;
                }
            }
        }
    }

    @Override
    public void EncodeUnicode() {
        for (int i = 0; i < arrayOfChars.length; i++) {
            for (int j = 0; j < key; j++) {
                arrayOfChars[i]++;
            }
        }
    }

    @Override
    public void EncodeShift() {
        for (int i = 0; i < arrayOfChars.length; i++) {
            for (int j = 0; j < key; j++) {
                if (arrayOfChars[i] >= 'A' && arrayOfChars[i] <= 'Z') {
                    if (arrayOfChars[i] + 1 > 'Z')
                        arrayOfChars[i] = 'A';
                    else
                        arrayOfChars[i]++;
                }
                if (arrayOfChars[i] >= 'a' && arrayOfChars[i] <= 'z') {
                    if (arrayOfChars[i] + 1 > 'z')
                        arrayOfChars[i] = 'a';
                    else
                        arrayOfChars[i]++;
                }

            }
        }
    }
    private void Printer(){
        if (out == null) {
            for (char i :
                    arrayOfChars) {
                System.out.print(i);

            }
        }else{
            StringBuilder sb = new StringBuilder();
            for (char i :
                    arrayOfChars) {
                sb.append(i);
            }
            try {
                fw.write(sb.toString());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
