import com.yzk18.GUI.GUI;

public class ProgressDialogTest1 {
    public static void main(String[] args) throws InterruptedException {
        GUI.showIndeterminateProgressDialog("hello");
        Thread.sleep(1000);
        GUI.showIndeterminateProgressDialog("33");
        Thread.sleep(3000);
        GUI.showIndeterminateProgressDialog("bb");
        Thread.sleep(300);
        GUI.closeIndeterminateProgressDialog();
    }
}
