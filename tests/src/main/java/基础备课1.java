import java.util.Arrays;

public class 基础备课1 {
    public static void main(String[] args) {
        /*
        int i;
        for(i=0;i<=3;i++);
        {
            System.out.println(i);
        }*/
        /*
        int i=5;
        if(i==6);
        {
            System.out.println("666");
        }*/
        /*
        int i=5;
        if(i=6)
        {
            System.out.println("666");
        }
        else
        {
            System.out.println("555");
        }*/
        /*
        for(int i=0;i<3;i++)
        {
            for(int j=1;j<=3;j++)
            {
                System.out.println(i+","+j);
            }
        }*/
        /*
        for(int i=1;i<=4;i++)
        {
            System.out.print(i);
            for(int j=2;j<=4;j++)
            {
                System.out.print(j);
            }
            System.out.println();
        }*/
        /*
        for (int i = 0; i < 5; i++)
        {
            if (i==3)
            {
                continue;
            }
            System.out.println(i);
        }*/
        /*
        String[] names = {"yzk","zack","yang"};
        for(int i=names.length-1;i>=0;i--)
        {
            System.out.println(names[i]);
        }*/
/*
        int nums1[]={3,8,9};
        int nums2[]={5,2,8};
        if(nums1.length!=nums2.length)
        {
            System.out.println("长度不一样");
            return;
        }
        int nums3[]=new int[nums1.length];
        for (int i=0;i<nums1.length;i++)
        {
            nums3[i]=nums1[i]+nums2[i];
        }
        System.out.println(Arrays.toString(nums3));*/
        /*
        int nums[] = { 3, 5, 999, 2, 12 };
        int max = nums[0]; // nums[0]默认为最大值“当前最大值”
        for (int i = 1; i < nums.length; i++) // 下标从 1 开始
        {
            // 如果遍历到的数比如“当前最大值”还大，则把“当前最大值”更新成为这个更大的数
            if (nums[i] > max) // 元素大小比较
            {
                max = nums[i]; // 保存最大值
            }
        }
        System.out.println("max=" + max); // 输出最大值*/
        /*
        int[] nums = {888,666,886,86,365};
        for (int n : nums)
        {
            System.out.println(n);
        }*/
        String[] names = {"yzk18","zack","yzk","杨中科","帅帅帅"};
        for(String name : names)
        {
            if(name.equals("杨中科"))
                continue;
            System.out.println(name);
        }
    }
}
