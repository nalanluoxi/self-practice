package 稀土掘金;

/**
 * @Author 纳兰洛熙
 * @Package：稀土掘金
 * @Project：LanQiaoBei
 * @name：数字字符串格式化
 * @Date：2024/12/19 17:00
 * @Filename：数字字符串格式化
 */
public class 数字字符串格式化 {


    public static void main(String[] args) {
        System.out.println(solution("987654321"));
        //System.out.println(solution("0000123456789.99").equals("123,456,789.99"));
        //System.out.println(solution("987654321").equals("987,654,321"));



       // System.out.println(res);

    }


    public static String solution(String s) {
        // write code here
        String res ="";
        int index=-1;

        for (int i = s.length()-1; i >=0; i--) {
            char c = s.charAt(i);
            res=c+res;
            if (c=='.'){
                index =i;
                break;
            }
        }
        int count=0;

        if (index==-1){
            res="";
            for (int i=s.length()-1;i>=0;i--){
                if (count==3){
                    res =","+res;
                    count=0;
                }
                res=s.charAt(i)+res;
                count++;
            }
        }else {
            for (int i=index-1;i>=0;i--){
                if (count==3){
                    res =","+res;
                    count=0;
                }
                res=s.charAt(i)+res;
                count++;
            }
        }


        while (true){
            char c = res.charAt(0);
            if (c>='1'&&c<='9'){
                break;
            }
            if (c=='0'||c==','){
                res=res.substring(1);
            }
        }
        return res;
    }
}
