package net.sharksystem.aasp.android.example;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.sharksystem.aasp.android.AASP;
import net.sharksystem.aasp.android.AASPService;
import net.sharksystem.aaspandroid.R;

public class MainActivity extends AppCompatActivity {
    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create broadcast receiver
        ExampleAASPBroadcastReceiver br = new ExampleAASPBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AASP.BROADCAST_ACTION);
        this.registerReceiver(br, filter);

        // start service - which allows service to outlive unbind
        Intent aaspServiceCreationIntent = new Intent(this, AASPService.class);
        aaspServiceCreationIntent.putExtra(AASP.USER, "alice");
        aaspServiceCreationIntent.putExtra(AASP.FOLDER, "alice");

        this.startService(aaspServiceCreationIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service
        bindService(new Intent(this, AASPService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        // kill it
        this.stopService(new Intent(this, AASPService.class));
    }

    public void onClick(View view) {
        View writeButton = findViewById(R.id.writeButton);

//        if(view == writeButton) {
            // Create and send a message to the service, using a supported 'what' value
            Message msg = Message.obtain(null, AASP.WRITE_MESSAGE, 0, 0);
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
//        }
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };
}