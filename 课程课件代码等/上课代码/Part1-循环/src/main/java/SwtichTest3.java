public class SwtichTest3 {
    public static void main(String[] args) {
        int m = 5;
        switch (m)
        {
            case 1:
                break;
            case 2:
                System.out.println("a");
                break;
            case 5:
            case 8:
                System.out.println("b");
                break;
            default:
                System.out.println("c");
                break;
        }

    }
}
