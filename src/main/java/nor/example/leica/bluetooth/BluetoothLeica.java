package nor.example.leica.bluetooth;

import nor.example.leica.notification.LeicaNotification;
import nor.example.leica.notification.ValueNotification;
import tinyb.*;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class BluetoothLeica implements BluetoothNotification<Boolean>, Serializable {
    private static final long serialVersionUID = -1093810189395001L;
    private static final Logger log = Logger.getLogger(BluetoothLeica.class.getName());
    private static BluetoothLeica bluetoothLeica = new BluetoothLeica();

    private static boolean instancedFlag = false;
    private static boolean isEnableLeicaNotificationFlag = false;
    private static boolean isEnableValueNotificationFlag = false;



    private static final long SEARCH_DEVICE_TIMEOUT = 5; //searching device time in seconds
    private static final long GET_DEVICE_TIME = 20; //get list of device

    static boolean isConnected = false;
    static boolean running = true;

    private static Object mContext;
    private static String mDeviceAddress;

    private static LeicaNotification mListener;
    private ValueNotification valueNotification = new ValueNotification();

    BluetoothDevice mSensor;


    Lock mLock;
    Condition mCondition;


    private BluetoothLeica() {
        if (bluetoothLeica != null) {
            throw new IllegalStateException("Instance of BluetoothLeica already created!");
        }
        mLock = new ReentrantLock();
        mCondition = mLock.newCondition();
    }

    public static BluetoothLeica getInstance(@Nonnull Object context, String deviceAddress, LeicaNotification listener) {

        if (!instancedFlag) {
            instancedFlag = true;
            mContext = context;
            mDeviceAddress = deviceAddress;
            mListener = listener;
        }

        return bluetoothLeica;
    }

    public Boolean execute(@Nonnull String command) {
        Runnable task = () -> {

//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


//            List<BluetoothGattService> bluetoothServices = null;
//            Duration time = Duration.parse("PT4.0S");

            BluetoothGattService distoService = null;
            try {
                distoService = getService(mSensor, DISTOtransfer.DISTO_SERVICE.toString());
            } catch (InterruptedException e) {
                //todo return this exception
                e.printStackTrace();
            }

            if (distoService == null) {
                System.err.println("This device does not have the service we are looking for.");
                mSensor.disconnect();
                mListener.ServiceNotFound(mSensor, DISTOtransfer.DISTO_SERVICE);

                System.exit(-1);
            }
            System.out.println("Found service " + distoService.getUUID());

            BluetoothGattCharacteristic distanceCharacteristic = getCharacteristic(distoService, DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE.toString());

            if (!isEnableValueNotificationFlag) {
                distanceCharacteristic.enableValueNotifications(valueNotification);
                isEnableValueNotificationFlag = true;
            }

            BluetoothGattCharacteristic commandMeasureCharacteristic = getCharacteristic(distoService, DISTOtransfer.DISTO_CHARACTERISTIC_COMMAND.toString());

            // 'g' = {0x67};
            byte[] byteCommand = command.getBytes();
            boolean resp = commandMeasureCharacteristic.writeValue(byteCommand);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] val = distanceCharacteristic.readValue();
            float distance = ByteBuffer.wrap(val).order(ByteOrder.LITTLE_ENDIAN).getFloat();

            mListener.GetMeasurament(distance);
        };
        Thread thread = new Thread(task);
        thread.setName("Execute_BluetoothLeica_thread");
        thread.start();

        log.info("Fine " + Thread.currentThread().getName());

        return true;
    }

    public Boolean connect() {

        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Inizio " + threadName);
            /*
             * To start looking of the device, we first must initialize the TinyB library. The way of interacting with the
             * library is through the BluetoothManager. There can be only one BluetoothManager at one time, and the
             * reference to it is obtained through the getBluetoothManager method.
             */
            BluetoothManager manager = BluetoothManager.getBluetoothManager();

            /*
             * The manager will try to initialize a BluetoothAdapter if any adapter is present in the system. To initialize
             * discovery we can call startDiscovery, which will put the default adapter in discovery mode.
             */
            boolean discoveryStarted = manager.startDiscovery();

            System.out.println("The discovery started: " + (discoveryStarted ? "true" : "false"));
            mSensor = getDevice(mDeviceAddress);

            /*
             * After we find the device we can stop looking for other devices.
             */
            try {
                manager.stopDiscovery();
            } catch (BluetoothException e) {
                log.info("Discovery could not be stopped.");
            }

            if (mSensor != null) {
                try {
                    boolean con = mSensor.connect();
                    if (con) {
                        System.out.println("Sensor with the provided address connected");
                        mListener.Connected(mSensor);
                        isConnected = true;

                        if (!isEnableLeicaNotificationFlag) {
                            mSensor.enableConnectedNotifications(this);
                            isEnableLeicaNotificationFlag = true;
                        }

                    } else {
                        System.out.println("Could not connect device.");
                        mListener.DeviceNotConnected(mSensor);
                    }
                } catch (BluetoothException e) {
                    log.info("Unable to connect device: " + e.getMessage());
                    mListener.UnableToConnect();
                }

                //prevent shutdown application
                addShutdownHook();

            }

            log.info("Fine connect thread" + Thread.currentThread().getName());
        };

        Thread thread = new Thread(task);
        thread.setName("Connect_BluetoothLeica_thread");
        thread.start();

        log.info("Fine " + Thread.currentThread().getName());
        return true;
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("Fine " + Thread.currentThread().getName());

                running = false;
                isConnected = false;
                instancedFlag = false;
                isEnableLeicaNotificationFlag = false;
                isEnableValueNotificationFlag = false;

                mLock.lock();
                try {
                    mCondition.signalAll();
                } finally {
                    mLock.unlock();
                    if (mSensor != null) {
                        mSensor.disconnect();
                        log.info("ShutdownHook " + Thread.currentThread().getName());
                    }
                }
            }
        });
    }

    @Override
    public void run(Boolean aBoolean) {
        log.info("----->>Thread name: " + Thread.currentThread().getName());
        if (aBoolean) {
            //todo maybe a bug, the first time that attempts to connect, run is not fired!!!
            System.out.println("Connected!");
        } else {
            System.out.println("Disconnected!!");
            //device shutdown
            if (isConnected) {
                mListener.DeviceShutdown();
            }
        }
    }

    public boolean disconnect(String deviceAddress) {
        log.info("Fine " + Thread.currentThread().getName());
        if (deviceAddress.equals(mDeviceAddress) && mSensor != null) {

            isConnected = false;
            mSensor.disconnect();

            mListener.Disconnected(mSensor);
            return true;
        }
        return false;
    }

    /*
     * Our device should expose a temperature service, which has a UUID we can find out from the data sheet. The service
     * description of the SensorTag can be found here:
     * http://processors.wiki.ti.com/images/a/a8/BLE_SensorTag_GATT_Server.pdf. The service we are looking for has the
     * short UUID AA00 which we insert into the TI Base UUID: f000XXXX-0451-4000-b000-000000000000
     */
    static BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
        System.out.println("Services exposed by device:");
        BluetoothGattService tempService = null;
        List<BluetoothGattService> bluetoothServices = null;
        do {
            bluetoothServices = device.getServices();
            if (bluetoothServices == null)
                return null;

            for (BluetoothGattService service : bluetoothServices) {
                System.out.println("UUID: " + service.getUUID());
                if (service.getUUID().equals(UUID))
                    tempService = service;
            }
            Thread.sleep(2000);
        } while (bluetoothServices.isEmpty() && running);
        return tempService;
    }

    static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
        if (characteristics == null)
            return null;

        for (BluetoothGattCharacteristic characteristic : characteristics) {
            if (characteristic.getUUID().equals(UUID))
                return characteristic;
        }
        return null;
    }

    /*
     * After discovery is started, new devices will be detected. We can get a list of all devices through the manager's
     * getDevices method. We can the look through the list of devices to find the device with the MAC which we provided
     * as a parameter. We continue looking until we find it, or we try 15 times (1 minutes).
     * If the device is not discovered in 15 second we get a time out exception.
     */
    private static BluetoothDevice getDevice(String address) {

        ExecutorService executorService = Executors.newCachedThreadPool();
//        Future<BluetoothDevice> task = executorService.submit(() -> publisher.publish(article));

        Callable<BluetoothDevice> task = new Callable<BluetoothDevice>() {
            public BluetoothDevice call() throws InterruptedException {

                BluetoothManager manager = BluetoothManager.getBluetoothManager();
                BluetoothDevice sensor = null;
                for (int i = 0; (i < GET_DEVICE_TIME) && running; ++i) {
                    List<BluetoothDevice> list = manager.getDevices();
                    if (list == null)
                        return null;

                    for (BluetoothDevice dev : list) {
                        if (dev.getAddress().equals(address)) {
                            return dev;
                        }
                    }
                    Thread.sleep(1000);
                    System.out.println(i);
                }
                return null;
            }
        };

        Future<BluetoothDevice> future = executorService.submit(task);
        try {
            BluetoothDevice result = future.get(SEARCH_DEVICE_TIMEOUT, TimeUnit.SECONDS);

            if (result == null) {
                System.out.println("No mSensor found with the provided address. Device is null!");
                mListener.NoDeviceFound();
            } else {
                System.out.print("Found device: ");
                printDevice(result);
                mListener.DeviceFound(result);
            }
            return result;
        } catch (TimeoutException ex) {
            // handle the timeout
            System.out.println("TimeoutException");
            mListener.SearchingDeviceTimeout();
        } catch (InterruptedException e) {
            // handle the interrupts
            System.out.println("InterruptedException");

        } catch (ExecutionException e) {
            // handle other exceptions
            System.out.println("ExecutionException");
        } finally {
            future.cancel(true);
        }
        System.out.println("NoDeviceFound");
        mListener.NoDeviceFound();
        return null;
    }

    static void printDevice(BluetoothDevice device) {
        System.out.print("Address = " + device.getAddress());
        System.out.print(" Name = " + device.getName());
        System.out.print(" Connected = " + device.getConnected());
        System.out.println();
    }
}
