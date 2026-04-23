package pactice;

import java.util.HashMap;
import java.util.Map;

public class CanConstruct {

    public static void main(String[] args) {
        String ransomNote="aa";
        String magazine="ab";
        canConstruct(ransomNote,  magazine);
    }
    public static boolean canConstruct(String ransomNote, String magazine) {
       int []count=new int[26];

       if (ransomNote.length()>magazine.length()){
           return false;
       }

        for (char m: magazine.toCharArray()) {
            count[m-'a']+=1;
        }

        for (char n:ransomNote.toCharArray()) {
            count[n-'a']-=1;
        }

        for (int i:count) {
            if (i<0){
                System.out.println("false");
                return false;
            }
        }
        System.out.println("true");
        return true;
    }

}
