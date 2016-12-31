package Driver.UsbDriver;

/**
 * Created by think on 2016/10/17.
 */

public interface IUsbStoragePort extends IUsbBasePort {
    void switchMode(byte[] data);
}
