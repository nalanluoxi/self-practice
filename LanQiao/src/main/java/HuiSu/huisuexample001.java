package HuiSu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class huisuexample001 {

    /*存总体结果集合的集合*/
    static List<List<Integer>> resultlist=new ArrayList<>();

    /*存单个结果的集合*/
    static LinkedList<Integer> answer=new LinkedList<>();


    public List<List<Integer>> combinationSum3(int k, int n) {
        backing(k,n,0,1);
        return resultlist;
    }

    public static void backing(int k,int n,int sum,int startindex){

        if (answer.size()==k){
            if (sum==n){
                resultlist.add(new ArrayList<>(answer));
            }
        }

        if (sum>n){
            return;
        }

        for (int i = startindex; i <=9-(k-startindex)+1 ; i++) {
            sum+=i;
            answer.add(i);
            backing(k,n,sum,i+1);
            sum-=i;
            answer.removeLast();
        }
    }


}
