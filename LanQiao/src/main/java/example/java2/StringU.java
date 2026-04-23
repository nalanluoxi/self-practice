package example.java2;

public class StringU {

    public static void printname(String name){
        if (name!=null){
            System.out.println(name);
        }
    }

    public  static int getLeng(String date){
        if (date==null){
            return -1;
        }
        return  date.length()-1;
    }



}
