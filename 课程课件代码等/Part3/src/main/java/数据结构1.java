import java.util.Arrays;
import java.util.LinkedList;

public class 数据结构1 {
    public static void main(String[] args) {
        /*
        int[] nums1 = new int[]{3,8,9};
        int[] nums2 = nums1;
        nums1 = new int[]{3,8,9,10};
        System.out.println(Arrays.toString(nums1));
        System.out.println(Arrays.toString(nums2));*/
        LinkedList list1 = new LinkedList();
        list1.add(3);
        list1.add(6);
        list1.add(5);
        list1.add("xxxx");

        /*
        System.out.println(list1.size());
        System.out.println(list1.get(1));
        System.out.println(list1.get(2));*/
        //System.out.println(list1.get(3));

        for(int i=0;i<list1.size();i++)
        {
            System.out.println(list1.get(i));
        }
        /*
        for(Object obj : list1)
        {
            System.out.println(obj);
        }*/
    }
}
