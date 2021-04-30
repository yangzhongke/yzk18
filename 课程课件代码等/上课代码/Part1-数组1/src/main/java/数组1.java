public class 数组1 {
    public static void main(String[] args) {
        /*
        String s1 = "张三";
        String s2 = "罗翔";
        String s3 = "yzk";*/
        int[] nums1={3,5,9,88,66,3};
        int[] nums2=new int[8];
        System.out.println(nums1[2]);
        System.out.println(nums2[1]);
        nums2[1]=8;
        System.out.println(nums2[1]);
        nums1[0]=nums1[1]+nums2[1];//5+8
        System.out.println(nums1[0]);
        System.out.println(nums1[5]);
        System.out.println(nums1[6]);
    }
}
