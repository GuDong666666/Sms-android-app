package com.kode.smsmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kode.smsmanagement.model.Message;

/**
 * Created by Ermano
 * on 1/7/2018.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    Message m = new Message();
                    m.setDatecreated(Session.getDateAndTime());
                    m.setReceiver("Me");
                    m.setSender(senderNum);
                    m.setMessage(message);
                    m.setOperator(Session.getOperatorName(context));
                    m.setConversation(senderNum);
                    m.save();

                    if (Session.getHandler() != null){
                        android.os.Message hMsg = new android.os.Message();
                        hMsg.what = 1;
                        Session.getHandler().sendMessage(hMsg);
                    }


                    // Show Alert
                   /* int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();*/

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
