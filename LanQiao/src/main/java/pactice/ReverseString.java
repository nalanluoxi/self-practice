package pactice;

public class ReverseString {
    public static void main(String[] args) {
        char []s={'h','e','l','l','o'};
        reverseString(s);
    }

    public static void reverseString(char[] s) {
        int z=s.length-1;
        for (int i = 0; i < s.length; i++) {
            char temp=s[i];
            s[i]=s[z];
            s[z]=temp;
            z--;
            if (z==i||z<i){
                break;
            }
        }


        for (char c:s
             ) {
            System.out.println(c);

        }
    }

  /*  public static void reverseString(char[] s) {
        int l = 0;
        int r = s.length - 1;
        while(l < r){
            char temp = s[l];
            s[l] = s[r];
            s[r] = temp;
            l++;
            r--;
        }
    }*/
}
