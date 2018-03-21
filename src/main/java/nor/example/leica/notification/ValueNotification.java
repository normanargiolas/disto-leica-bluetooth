package nor.example.leica.notification;

import tinyb.BluetoothNotification;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by NormaN on 21/03/18.
 **/


public class ValueNotification implements BluetoothNotification<byte[]> {

    @Override
    public void run(byte[] bytes) {
        System.out.print("-------------------------");
        float distance = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        System.out.print(Float.toString(distance));
    }
}
