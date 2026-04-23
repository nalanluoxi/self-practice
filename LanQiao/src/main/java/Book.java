public class Book {

    private String auther;
    private String name;
    private  int num;

    public Book(String auther, String name, int num) {
        this.auther = auther;
        this.name = name;
        this.num = num;
    }

    public Book() {



    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
