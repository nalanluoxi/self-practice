package pactice;

public class fanzhuanzifuchuan {
    public static void main(String[] args) {

        String[] s= new String[5];
        s= new String[]{"h", "e", "l", "l", "o"};
        reverseString(s);
    }

    public static void reverseString(String[] s) {
        int len=s.length-1;
        System.out.print("[");
        for (int i = len; i>=0; i--) {
            if (i!=len){
                System.out.print(",");
            }
            System.out.print("\""+s[i]+"\"");
            if (i==0){
                System.out.print("]");
            }
        }
    }


}
