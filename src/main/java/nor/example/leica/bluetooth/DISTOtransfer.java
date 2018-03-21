/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nor.example.leica.bluetooth;

import java.util.UUID;

/**
 * This is the main Activity that displays the current chat session.
 */
public class DISTOtransfer {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_BLUETOOTH_ERROR = 6;
    public static final int MESSAGE_DISTOERROR = 7;
    public static final int MESSAGE_KEYBOARD = 8;
    public static final int MESSAGE_RESULT = 9;
	public static final int MESSAGE_UNABLE_TO_CONNECT = 10;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String DEVICE_ISBLE = "device_isble";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 2;
    public static final int REQUEST_ENABLE_BT = 3;


    public static final int MESSAGE_FIRST_TRY = 11;
    private static final String[] ALLOWED_DEVICES = new String[]{"DISTO".toLowerCase(), "WDM".toLowerCase(), "STABILA".toLowerCase(), "DEWALT".toLowerCase(), "STANLEY".toLowerCase()};

    public static final UUID DISTO_SERVICE                                           = UUID.fromString("3ab10100-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_DISTANCE                           = UUID.fromString("3ab10101-f831-4395-b29d-570977d5bf94"); //read-indicate
    public static final UUID DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT              = UUID.fromString("3ab10102-f831-4395-b29d-570977d5bf94"); //read-indicate
    public static final UUID DISTO_CHARACTERISTIC_INCLINATION                        = UUID.fromString("3ab10103-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_INCLINATION_DISPLAY_UNIT           = UUID.fromString("3ab10104-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION               = UUID.fromString("3ab10105-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION_DISTPLAY_UNIT = UUID.fromString("3ab10106-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_HORIZONTAL_INCLINE                 = UUID.fromString("3ab10107-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_VERTICAL_INCLINE                   = UUID.fromString("3ab10108-f831-4395-b29d-570977d5bf94");
    public static final UUID DISTO_CHARACTERISTIC_COMMAND                            = UUID.fromString("3ab10109-f831-4395-b29d-570977d5bf94"); //write-whithout-responce
    public static final UUID DISTO_CHARACTERISTIC_STATE_RESPONSE                     = UUID.fromString("3ab1010A-f831-4395-b29d-570977d5bf94"); //read-indicate
    public static final UUID DISTO_DESCRIPTOR                                        = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final int MESSAGE_DEBUG = 3199;

    public static final UUID DISTO_CHARACTERISTIC_UNKNOWN1                     = UUID.fromString("3ab1010c-f831-4395-b29d-570977d5bf94"); //read


//    public static boolean isValidDevice(BluetoothDevice dev) {
//
//        String name = dev.getName().toLowerCase();
//        for (String allowed : ALLOWED_DEVICES) {
//            if (name.startsWith(allowed))
//                return true;
//        }
//        return false;
//    }

    public static boolean isValidDevice(String realName) {
        String name = realName.toLowerCase();
        for (String allowed : ALLOWED_DEVICES) {
            if (name.startsWith(allowed))
                return true;
        }
        return false;
    }
}
