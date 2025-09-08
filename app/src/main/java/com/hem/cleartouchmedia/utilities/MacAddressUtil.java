package com.hem.cleartouchmedia.utilities;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MacAddressUtil {
    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            if (!TextUtils.isEmpty(macAddress)) {
                return macAddress;
            }
        }

        // On Android 6.0 and above, the MAC address might return as "02:00:00:00:00:00" or null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            } catch (Exception ex) {
                // Handle exception
            }
        }

        return "02:00:00:00:00:00"; // Return a default MAC address if none found
    }
}
