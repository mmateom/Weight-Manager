/*Copyright 2016 WorkStress Experiment
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.mikel.poseidon.Activities.utility;

import java.io.Serializable;

/**
 * Simple class for all Bluetooth devices
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
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
