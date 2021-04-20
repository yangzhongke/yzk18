import com.yzk18.GUI.GUI;

public class ProgressDialogTest2 {
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<100;i++)
        {
            GUI.showProgressDialog("导入中",100,i);
            Thread.sleep(100);
        }
        GUI.closeProgressDialog();
    }
}
