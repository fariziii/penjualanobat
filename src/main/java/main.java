import frame.kasirViewFrame;
import helpers.koneksi;

public class main {
    public static void main(String[] args) {
        koneksi.getConnection();
        kasirViewFrame viewFrame= new kasirViewFrame();
        viewFrame.setVisible(true);
    }
}
