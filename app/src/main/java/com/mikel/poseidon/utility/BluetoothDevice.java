package com.mikel.poseidon.utility;

import java.io.Serializable;

/**
 * Created by mikel on 05/04/2017.
 */

public class BluetoothDevice implements Serializable, Comparable<BluetoothDevice> {

    public String name;
    public String macaddress;
    public int rssi;
    public boolean checked;
    public static final String MAC_PREFIX = "MAC address: ";

    @Override
    public boolean equals(Object another) {

        boolean result = false;

        if (another != null && another instanceof BluetoothDevice) {
            result = macaddress.equals(((BluetoothDevice) another).macaddress);
        }

        return result;
    }

    @Override
    public int compareTo(BluetoothDevice another) {
        return another.rssi - rssi;
    }
}
