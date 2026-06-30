package acm练习题;


import java.util.*;

public class Test打伞人 {
    public static void main(String[] args) {
        // 示例测试用例1
        int[] start = {1, 3, 2};
        int[] end = {10, 6, 12};
        test(start, end);
        //System.out.println(Arrays.toString(ans));
        // 输出解释看下方示例推演
    }



    public static void test(int[]arr,int []brr){
        int len = arr.length;
        Persion [] persions=new Persion[len];
        for (int i = 0; i < len; i++) {
            persions[i]=new Persion(arr[i],brr[i],i);
        }
        List<Persion> team=new ArrayList<>();
        for (Persion persion : persions) {
            if (persion.isInTeam){
                team.add(persion);
            }
        }
        if (team.isEmpty()){
            return ;
        }
        Persion leader = getMinStart(team);
        while (!team.isEmpty()){
            int nextSTop = Integer.MAX_VALUE;
            for (Persion persion : team) {
                nextSTop = Math.min(persion.end, nextSTop);
            }
            int walk = nextSTop - leader.curPos;
            leader.sanTime+=walk;
            for (Persion persion : team) {
                persion.curPos=nextSTop;
            }
            List<Persion> newTeam=new ArrayList<>();
            for (Persion persion : team) {
                if (persion.curPos>=persion.end){
                    persion.isInTeam=false;
                }else {
                    newTeam.add(persion);
                }
            }
            team=newTeam;
            if (team.isEmpty()){
                break;
            }
            if (!leader.isInTeam){
                leader=getMinStart(team);
            }



        }

        for (Persion persion : persions) {
            System.out.println(persion.sanTime);
        }

    }

    public static Persion getMinStart(List<Persion> team){
        Persion min = team.get(0);
        for (Persion persion : team) {
            if (persion.curPos<min.curPos){
                min=persion;
            }
        }
        return min;
    }

    static class Persion{
        int start;
        int end;
        boolean isInTeam;
        int sanTime;
        int id;
        int curPos;

        public Persion(int start, int end, int id) {
            this.start = start;
            this.end = end;
            this.sanTime = 0;
            this.id = id;
            this.curPos = start;
            if (end<start){
                this.isInTeam=false;
            }else {
                this.isInTeam=true;
            }
        }
    }

}
