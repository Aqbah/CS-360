package com.example.aqbahinventoryapp;

import android.content.Context;
import android.telephony.SmsManager;

public class SmsUtils {

    public static void sendLowInventorySms(Context ctx, String phone, InventoryItem item) {
        // NOTE: This requires SEND_SMS permission granted at runtime.
        // On emulator, use numbers like 5554/5556 (or as configured).
        String msg = "Low inventory alert: " + item.name +
                " (Qty: " + item.quantity + ", Threshold: " + item.threshold + ")";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, msg, null, null);
    }
}
