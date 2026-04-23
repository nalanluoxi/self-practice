package pactice;

public class ReverseWords {

    public static void main(String[] args) {

        String s = "the sky is blue";
        reverseWords(s);
    }
    public static String reverseWords(String s) {
        String[]ns=s.split(" ");
        String[]result=new String[ns.length];
        int len=ns.length-1;
        for (String temp:ns) {
            result[len]=temp;
            len--;
        }
        s=String.join("",result);
        return s;

    }

}
