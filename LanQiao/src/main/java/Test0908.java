public class Test0908 {

    //身份证 邮箱
    //身份证 展示6*4
    //邮箱 2***2@后面正常


    public static void main(String[] args) {
        String str1="123456789009876543";
        String str2="123456789009876543";

        String str3="zxccxz@qq.com";
        String str4="12345678@qq.com";


        System.out.println(test(str1));
        System.out.println(test(str2));
        System.out.println(test(str3));
        System.out.println(test(str4));
    }

    public static String test(String string){
        String ans="";

        if (string.contains("@")){
            int index = string.indexOf("@");
            String[] split = string.split("");
            for (int i = 0; i < string.length(); i++) {
                if (i>=index){
                    ans+=split[i];
                }else if (i>=2 && i<index-2){
                    ans+="*";
                }else {
                    ans+=split[i];
                }
            }
        }else if (string.length()==18){
            String[] split = string.split("");
            for (int i = 0; i < split.length; i++) {
                if (i<6 || i>=14){
                    ans+=split[i];
                }else {
                    ans+="*";
                }
            }
        }else {
            return "-1";
        }

        return ans;
    }
}
