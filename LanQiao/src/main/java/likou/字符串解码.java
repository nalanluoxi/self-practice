package likou;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



/**
 * @Author 纳兰洛熙
 * @Package：likou
 * @Project：LanQiaoBei
 * @name：字符串解码
 * @Date：2025/2/8 10:36
 * @Filename：字符串解码
 */
public class 字符串解码 {

    public static void main(String[] args) {
        System.out.println(new 字符串解码().decodeString("3[a]2[bc]" ));
     //   System.out.println(new 字符串解码().decodeString("3[a2[c]]" ));
    }


    static int ptr;
    public String decodeString(String s) {
        LinkedList<String> stack=new LinkedList<>();
        ptr=0;

        while (ptr<s.length()){
            char cur = s.charAt(ptr);
            if (Character.isDigit(cur)){
                String digit = getDigits(s);
                stack.addLast(digit);
            } else if (Character.isLetter(cur)||cur=='[') {
                stack.addLast(String.valueOf(s.charAt(ptr++)));
            }else {
                ptr++;
                LinkedList<String> sub = new LinkedList<>();
                while (!"[".equals(stack.peekLast())){
                    sub.addLast(stack.removeLast());
                }
                Collections.reverse(sub);
                String o=getString(sub);
                stack.removeLast();
                int repTime=Integer.parseInt(stack.removeLast());
                StringBuffer t=new StringBuffer();
                while (repTime>0){
                    t.append(o);
                    repTime--;
                }
                stack.addLast(t.toString());
            }
        }
        return getString(stack);
    }

    private String getString(LinkedList<String> sub) {
        StringBuffer sb=new StringBuffer();
        for (String s:sub){
            sb.append(s);
        }
        return sb.toString();
    }

    private String getDigits(String s) {
        String ret="";
        while (Character.isDigit(s.charAt(ptr))){
            ret+=s.charAt(ptr++);
        }
        return ret;
    }
}
