package nor.example.leica.notification;

import tinyb.BluetoothDevice;

import java.util.UUID;

public interface LeicaNotification {
    void NoDeviceFound();

    void SearchingDeviceTimeout();

    void DeviceFound(BluetoothDevice result);

    void Connected(BluetoothDevice sensor);
    void Disconnected(BluetoothDevice sensor);

    void DeviceNotConnected(BluetoothDevice sensor);


    void UnableToConnect();

    void DeviceShutdown();

    void GetMeasurament(float measurament);

    void ServiceNotFound(BluetoothDevice mSensor, UUID distoService);
}
