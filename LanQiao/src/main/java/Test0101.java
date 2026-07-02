import javax.swing.text.StyledEditorKit;

public class Test0101 {

    public static void main(String[] args) {
        int[]arr={1,3,5,7,2,4,6,8};
        int k=4;
        test(arr,k);
    }

    public static void test(int []arr,int k){
        sort(arr,0,arr.length-1);
        for (int i = 0; i < k; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void sort(int []arr,int left,int right){
        if (left>=right){
            return;
        }
        int mid=left+(right-left)/2;
        sort(arr,left,mid);
        sort(arr,mid+1,right);
        addTwo(arr,left,mid,right);
    }

    public static void addTwo(int[]arr,int left,int mid ,int right){
        int []temp=new int[right-left+1];
        int i=left,j=mid+1;
        int k=0;
        while (i<=mid && j<=right){
            if (arr[i]<=arr[j]){
                temp[k++]=arr[i++];
            }else {
                temp[k++]=arr[j++];
            }
        }
        while (i<=mid){
            temp[k++]=arr[i++];
        }
        while (j<=right){
            temp[k++]=arr[j++];
        }
        for (int l = 0; l < temp.length; l++) {
            arr[l+left]=temp[l];
        }
    }

}
