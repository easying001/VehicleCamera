package Driver;

/**
 * Created by think on 2016/9/28.
 */

public class UsbRequest {
    public static final int USB_SET_REQUEST = 0x21;
    public static final int USB_GET_REQUEST = 0xA1;

    public static final int USB_UVC_UNIT_ID = 0x0400;
    public static final int USB_SUB_CMD_GET_DEV_TIME = 0x00;
    public static final int USB_SUB_CMD_SET_DEV_TIME = 0x01;
    public static final int USB_SUB_CMD_GET_DEV_PARAM = 0x03;
    public static final int USB_SUB_CMD_GET_DEV_VERSION = 0x04;
    public static final int USB_SUB_CMD_SET_GSENSOR_SENSITIVITY = 0x06;
    public static final int USB_SUB_CMD_SET_RESET_FACTORY = 0x08;

    public static final int USB_SUB_CMD_GET_VIDEO_PARAM = 0x00;
    public static final int USB_SUB_CMD_SET_VIDEO_PARAM = 0x01;

    public static final int USB_SUB_CMD_SET_RECORD_TAKEPIC = 0x00;
    public static final int USB_SUB_CMD_GET_RECORD_STATE = 0x01;
    public static final int USB_SUB_CMD_SET_RECORD_STATE = 0x02;
    public static final int USB_SUB_CMD_SET_RECORD_MIC_LEVEL = 0x04;
    public static final int USB_SUB_CMD_SET_RECORD_PROTECTED_RECORD = 0x06;


    public static final int USB_CAMERA_RECORD_STATE_STOPPED = 0x00;
    public static final int USB_CAMERA_RECORD_STATE_STARTED = 0x01;
    public static final int USB_CAMERA_RECORD_STATE_NO_SDCARD = 0x02;

    public static final int USB_SUB_CMD_GET_SDCARD_REMAINING = 0x00;
    public static final int USB_SUB_CMD_SET_SDCARD_FORMAT = 0x01;

    public static final int USB_SUB_CMD_SET_AUTH_CHALLENGE = 0x0A;
    public static final int USB_SUB_CMD_GET_AUTH_CHALLENGE = 0x0B;

    byte bmRequestType;
    byte requestCode;
    short wValue;
    short wIndex;
    short wLength;

    public UsbRequest(byte requestType, byte request, short value, short index, short length) {
        bmRequestType = requestType;
        requestCode = request;
        wValue = value;
        wIndex = index;
        wLength = length;
    }
}
