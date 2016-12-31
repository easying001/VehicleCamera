package Models;

/**
 * Created by think on 2016/9/23.
 */

public interface UsbCommunication {
    int controlOutTransfer(byte[] buffer, int length);
    int controlInTransfer(byte[] buffer, int length);

}
