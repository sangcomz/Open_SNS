/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.sangcomz.open_sns.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Member;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.ui.post.PostActivity;
import xyz.sangcomz.open_sns.util.Utils;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    SharedPref sharedPref;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        sharedPref = new SharedPref(getApplicationContext());
//        System.out.println("toString :::: " + data.toString());
        String message = data.getString("comment_content");
        String postSrl = data.getString("post_srl");
        String postWriterMember = data.getString("post_writer_member");
        String commentWriterMember = data.getString("comment_writer_member");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Gson gson = new Gson();
        Type type = new TypeToken<Member>() {
        }.getType();
        final Member postMember = gson.fromJson(postWriterMember, type);
        final Member commentMember = gson.fromJson(commentWriterMember, type);
        Log.d(TAG, "postWriterMember: " + postMember.getMemberName());
        Log.d(TAG, "commentWriterMember: " + commentMember.getMemberName());

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (sharedPref.getStringPref(SharedDefine.SHARED_PUSH_ON_OFF).equals("Y"))
            sendCommentNotification(message, postSrl, postMember, commentMember);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendCommentNotification(String message, String postSrl, Member postMember, Member commentMember) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("post_srl", postSrl);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(commentMember.getProfilePath());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_noti);
//            image = Bitmap.createBitmap(R.mipmap.stub,);
            e.printStackTrace();
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle(commentMember.getMemberName())
                .setLargeIcon(Utils.getCircleBitmap(getApplicationContext(), image))
                .setContentText(getString(R.string.txt_noti_comment) + message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
