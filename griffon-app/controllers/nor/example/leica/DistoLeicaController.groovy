package nor.example.leica

import griffon.core.artifact.GriffonController
import griffon.core.controller.ControllerAction
import griffon.inject.MVCMember
import griffon.metadata.ArtifactProviderFor
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import nor.example.leica.bluetooth.BluetoothLeica
import nor.example.leica.notification.LeicaNotification
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tinyb.BluetoothDevice

import javax.annotation.Nonnull

@ArtifactProviderFor(GriffonController)
class DistoLeicaController implements LeicaNotification {

    final static Logger log = LoggerFactory.getLogger(DistoLeicaController.class)

    private BluetoothLeica bluetoothLeica
    static boolean isConnect = false

    @MVCMember
    @Nonnull
    private DistoLeicaView view

    @MVCMember
    @Nonnull
    DistoLeicaModel model

    @ControllerAction
    def execute() {
        log.info("execute")

        String command = model.sampleForm.getCommand()
        if (isConnect == true) {
            if (!"".equals(command)) {
                bluetoothLeica.execute(command)
                logAction("Executing command [${command}] ...")
            } else {
                logAction("No command selected!")
            }
        }else{
            logAction("No device connected!")
        }
    }

    @ControllerAction
    def connection() {
        log.info("connection")

        String deviceAddress = model.sampleForm.getAddress()
        if (isConnect == false) {
            logAction("Connecting...")
            bluetoothLeica = BluetoothLeica.getInstance(this, deviceAddress, this)
            bluetoothLeica.connect()
        } else {
            logAction("Disconnecting...")
            bluetoothLeica.disconnect(deviceAddress)
        }
    }

    private void logAction(String msg) {
        TextArea text = view.getGridPane().lookup(SelectorView.OUTPUT_TXTAREA.getSelector())
        text.appendText(msg + "\n")
        text.setScrollTop(Double.MAX_VALUE)
    }

    @Override
    void NoDeviceFound() {
        log.info("NoDeviceFound")
        logAction("No device found!")
    }

    @Override
    void SearchingDeviceTimeout() {
        log.info("SearchingDeviceTimeout")
        logAction("Searching device timeout!")
    }

    @Override
    void DeviceFound(BluetoothDevice device) {
        log.info("DeviceFound")
        logAction("Device found:")
        logAction("Address = " + device.getAddress())
        logAction("Name = " + device.getName())
        logAction("Connected = " + device.getConnected())
    }

    @Override
    void Connected(BluetoothDevice sensor) {
        log.info("Connected")

        Button button = view.getGridPane().lookup(SelectorView.CONNECT_BTN.getSelector())

        Platform.runLater(new Runnable() {
            @Override
            void run() {
                // if you change the UI, do it here !
                button.setText('Disconnetti')
                isConnect = true
                logAction("Connected!")
            }
        })
    }

    @Override
    void Disconnected(BluetoothDevice sensor) {
        log.info("Disconnected")

        Button button = view.getGridPane().lookup(SelectorView.CONNECT_BTN.getSelector())

        Platform.runLater(new Runnable() {
            @Override
            void run() {
                // if you change the UI, do it here !
                button.setText('Connetti')
                isConnect = false
                logAction("Disconnected!")
            }
        })
    }

    @Override
    void DeviceNotConnected(BluetoothDevice sensor) {
        log.info("DeviceNotConnected")
        logAction("Device not connected!")
    }

    @Override
    void UnableToConnect() {
        log.info("UnableToConnect")
        logAction("Unable to connect!")
    }

    @Override
    void DeviceShutdown() {
        log.info("DeviceShutdown")
        logAction("Device Shutdown!")
        Disconnected(null)
    }

    @Override
    void GetMeasurament(float measurament) {
        log.info("GetMeasurament")
        logAction("Measurament: ${measurament}")
    }

    @Override
    void ServiceNotFound(BluetoothDevice mSensor, UUID distoService) {
        log.info("ServiceNotFound")
        String service = distoService.toString()
        logAction("Service: ${service} not found!")
        Disconnected(null)
    }
}