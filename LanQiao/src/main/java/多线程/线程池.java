package 多线程;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author 纳兰洛熙
 * @Package：多线程
 * @Project：LanQiaoBei
 * @name：线程池
 * @Date：2025/5/21 22:20
 * @Filename：线程池
 */
public class 线程池 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

/*        int i=1;
        List<Integer>ans=new ArrayList<>();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i1 = i * (i + 1);
            System.out.println(i + " * " + (i + 1) + " = " + i1);
            ans.add(i1);
            return i1;
        }).thenApply((x) -> {
            int i2 = x * (x + 1);
            System.out.println(x + " * " + (x + 1) + " = " + i2);
            ans.add(i2);
            return i2;
        });
        Integer tans = future.get();
        System.out.println(tans);*/
/*        int i=1;
        CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
            return select(i);
        });
        List<String> list = future.get();
        for (String s : list) {
            System.out.println(s);
        }*/
        //demo1();
    }


    static Connection connection;
    public static void init() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/itheima?useSSL=false", "root", "1234");
    }
    public static void demo2() throws SQLException, ClassNotFoundException {
        init();

    }


    /*public static void demo1() {
        init();
        long start = System.currentTimeMillis();
        for (int i = 0; i < idtoScoreid.length; i++) {
            String[] split = idtoScoreid[i].split(":");
            String studentid = split[0];
            String courseid = split[1];
            String sn = idtoname.get(Integer.valueOf(studentid));
            String cn = idtoCourse.get(Integer.valueOf(courseid));
            System.out.println(sn + "要上" + cn + "课");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
        return ;
    }

    static Map<Integer,String> idtoname;
    static Map<Integer,String> idtoCourse;
    static String [] idtoScoreid;

    public static void init(){
        idtoname=new HashMap<>();
        idtoname.put(1,"张三");
        idtoname.put(2,"李四");
        idtoname.put(3,"王五");
        idtoCourse=new HashMap<>();
        idtoCourse.put(1,"语文");
        idtoCourse.put(2,"数学");
        idtoCourse.put(3,"英语");
        idtoScoreid=new String[6];
        idtoScoreid[0]="1:1";
        idtoScoreid[1]="1:2";
        idtoScoreid[2]="1:3";
        idtoScoreid[3]="2:1";
        idtoScoreid[4]="2:2";
        idtoScoreid[5]="3:2";
    }
    public static List<String> select(int a){
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> list = new ArrayList<>();
        int test=30;
        for (int i = 1; i < test; i++) {
            list.add("方法一第"+(i*test)+"个");
        }
        return list;
    }

    public static List<String> select2(int a){
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<String> list = new ArrayList<>();
        int test=30;
        for (int i = 0; i < test; i++) {
            list.add("方法二第"+(i*test)+"个");
        }
        return list;
    }*/
}
