package pactice;

public class CanCompleteCircuit {
    public static void main(String[] args) {

        int[]gas={1,2,3,4,5};
        int[]cost={3,4,5,1,2};
        int index = canCompleteCircuit(gas, cost);
        System.out.println(index);
    }

   /*public static int canCompleteCircuit(int[] gas, int[] cost) {

        //总油量小于总耗油量，直接结束
        if (addsum(gas)<addsum(cost)){
            return -1;
        }
        //gas与cost作差，获取一个数集，累计oil量
         int[]rest=new int[gas.length-1];
         for (int i = 0; i < rest.length; i++) {
             rest[i]=gas[i]-cost[i];
         }


         //查找开始索引，定义一个当前油量，开始序号start
         int start=0;
         int nowoil=0;
            for (int i = 0; i < rest.length; i++) {
                nowoil+=rest[i];//如果当前油量大于0，start不变，如果当前油量小于等于0，start拨动到当前i+1
                if (nowoil<=0){
                    start=i+1;
                }
            }
            nowoil=rest[start];
            //当前油量变为rest集合对应start
            int count=0;//定义计数器，如果计数器到达rest的长度，结束循环
            while (true){
                nowoil+=rest[start+1];
                if (nowoil>0){//油量大于0，索引后拨，计数器+1
                    start++;
                    count++;
                }
                if (nowoil<0){//当前油量小于0，证明不能循环
                    return -1;
                }
                if (start+1>rest.length-1){//如果索引+1为超出范围，拨动到0
                    start=0;
                }
                if (count==rest.length-1){//计数器等于长度，结束循环，返回开始索引
                    return start;
                }
            }
    }

    public static int addsum(int[] num){
        int sum=0;
        for (int i = 0; i < num.length; i++) {
            sum+=num[i];
        }
        return sum;
    }*/
   /*public static int canCompleteCircuit2(int[] gas, int[] cost) {
       int sum = 0;
       int min = 0;
       for (int i = 0; i < gas.length; i++) {
           sum += (gas[i] - cost[i]);
           min = Math.min(sum, min);
       }

       if (sum < 0) return -1;
       if (min >= 0) return 0;

       for (int i = gas.length - 1; i > 0; i--) {
           min += (gas[i] - cost[i]);
           if (min >= 0) return i;
       }

       return -1;
   }
*/
   public static int canCompleteCircuit(int[] gas, int[] cost) {
       int sum = 0;
       int min = 0;
       for (int i = 0; i < gas.length; i++) {
           sum += (gas[i] - cost[i]);
           min = Math.min(sum, min);
       }

       if (sum < 0) return -1;
       if (min >= 0) return 0;

       for (int i = gas.length - 1; i > 0; i--) {
           min += (gas[i] - cost[i]);
           if (min >= 0) return i;
       }

       return -1;
   }


}
