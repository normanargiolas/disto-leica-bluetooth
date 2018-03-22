package nor.example.leica

import griffon.core.RunnableWithArgs
import griffon.core.artifact.GriffonController
import griffon.core.controller.ControllerAction
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
class DistoLeicaController extends AbstractController {

    final static Logger log = LoggerFactory.getLogger(DistoLeicaController.class)

    private BluetoothLeica bluetoothLeica
    static boolean isConnect = false


    @Override
    void mvcGroupInit(@Nonnull Map<String, Object> args) {
        super.mvcGroupInit(args)

        application.eventRouter.addEventListener(EVENT_CONNECTED, {
            updateUI(EVENT_CONNECTED)
        } as RunnableWithArgs)

        application.eventRouter.addEventListener(EVENT_DISCONNECTED, {
            updateUI(EVENT_DISCONNECTED)
        } as RunnableWithArgs)

    }

    //todo check for garbage thread
    void onShutdownStart(application) {
        Disconnected(null)
    }

    private updateUI(event) {
        Platform.runLater(new Runnable() {
            @Override
            void run() {

                Button connectBTN = view.getGridPane().lookup(SelectorView.CONNECT_BTN.getSelector())
                Button commandBTN = view.getGridPane().lookup(SelectorView.COMMAND_BTN.getSelector())

                switch (event) {
                    case EVENT_CONNECTED:
                        connectBTN.setDisable(false)
                        connectBTN.setText('Disconnetti')
                        isConnect = true
                        logAction("Connected!")
                        break
                    case EVENT_DISCONNECTED:
                        connectBTN.setDisable(false)
                        commandBTN.setDisable(false)

                        connectBTN.setText('Connetti')
                        isConnect = false
                        logAction("Disconnected!")
                        break
                    default:
                        log.info("Wow")
                        break
                }
            }
        })

    }

    @ControllerAction
    def execute() {
        log.info("execute")

        Button commandBTN = view.getGridPane().lookup(SelectorView.COMMAND_BTN.getSelector())
        commandBTN.setDisable(true)

        String command = model.sampleForm.getCommand()
        if (isConnect == true) {
            if (!"".equals(command)) {
                bluetoothLeica.execute(command)
                logAction("Executing command [${command}] ...")
            } else {
                commandBTN.setDisable(false)
                logAction("No command selected!")
            }
        } else {
            commandBTN.setDisable(false)
            logAction("No device connected!")
        }
    }

    @ControllerAction
    def connection() {
        log.info("connection")

        Button connectBTN = view.getGridPane().lookup(SelectorView.CONNECT_BTN.getSelector())
        connectBTN.setDisable(true)

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
        Platform.runLater(new Runnable() {
            @Override
            void run() {
                TextArea outputText = view.getGridPane().lookup(SelectorView.OUTPUT_TXTAREA.getSelector())

                outputText.appendText(msg + "\n")
                outputText.setScrollTop(Double.MAX_VALUE)
            }
        })
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
        application.eventRouter.publishEventAsync(EVENT_CONNECTED)
    }

    @Override
    void Disconnected(BluetoothDevice sensor) {
        log.info("Disconnected")
        application.eventRouter.publishEventAsync(EVENT_DISCONNECTED)
    }

    @Override
    void DeviceNotConnected(BluetoothDevice sensor) {
        log.info("DeviceNotConnected")
        logAction("Device not connected!")
        Disconnected(null)
    }

    @Override
    void UnableToConnect() {
        log.info("UnableToConnect")
        logAction("Unable to connect!")
        Disconnected(null)
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

        Button commandBTN = view.getGridPane().lookup(SelectorView.COMMAND_BTN.getSelector())
        commandBTN.setDisable(false)

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