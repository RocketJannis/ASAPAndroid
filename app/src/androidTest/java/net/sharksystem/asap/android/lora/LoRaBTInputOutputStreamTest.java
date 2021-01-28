package net.sharksystem.asap.android.lora;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import net.sharksystem.asap.android.lora.exceptions.ASAPLoRaException;
import net.sharksystem.asap.android.lora.exceptions.ASAPLoRaMessageException;
import net.sharksystem.asap.android.lora.messages.ASAPLoRaMessage;
import net.sharksystem.asap.android.lora.messages.AbstractASAPLoRaMessage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoRaBTInputOutputStreamTest {

    public static LoRaBTInputOutputStream Alice;
    public static LoRaBTInputOutputStream Bob;

    @BeforeClass
    public static void setup() throws IOException, InterruptedException {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        BluetoothDevice AliceBTDevice = null;
        BluetoothDevice BobBTDevice = null;

        for (BluetoothDevice btDevice : btAdapter.getBondedDevices()) {
            if (btDevice.getName().indexOf("ASAP-LoRa-1") == 0) {
                AliceBTDevice = btDevice;
            }
            if (btDevice.getName().indexOf("ASAP-LoRa-2") == 0) {
                BobBTDevice = btDevice;
            }
        }
        if (AliceBTDevice == null || BobBTDevice == null)
            throw new IOException("Please Pair BT Modules ASAP-LoRa-1 and ASAP-LoRa-2 to this device!");

        BluetoothSocket AliceSocket = AliceBTDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        BluetoothSocket BobSocket = BobBTDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

        AliceSocket.connect();
        BobSocket.connect();

        Thread.sleep(2000); //Give the BT Modules some time to stabilize

        Alice = new LoRaBTInputOutputStream(AliceSocket);
        Bob = new LoRaBTInputOutputStream(BobSocket);
    }

    @AfterClass
    public static  void teardown(){
        Alice.close();
        Bob.close();
    }

    @Test
    public void usesAppContext() {
        // Test if we are running in App Context
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("net.sharksystem.asap.example", appContext.getPackageName());
    }

    @Test(timeout = 10000)
    public void testASAPOutputToBTInput() throws IOException, ASAPLoRaMessageException {
        Alice.getASAPOutputStream("1001").write("Test".getBytes());
        Alice.flushASAPOutputStreams();

        while (true) {
            if (Bob.getInputStream().available() > 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(Bob.getInputStream()));
                String deviceResponse = br.readLine().trim();
                System.out.print("Test Device Response: ");
                System.out.println(deviceResponse);
                ASAPLoRaMessage asapMsg = (ASAPLoRaMessage) AbstractASAPLoRaMessage.createASAPLoRaMessage(deviceResponse);
                assertEquals(new ASAPLoRaMessage("1000", "Test".getBytes()).toString(), asapMsg.toString());
                break;
            }
        }
    }

    @Test(timeout = 100000)
    public void testMultipleASAPOutputToBTInput() throws IOException, ASAPLoRaMessageException {
        for (int i = 0; i < 10; i++) {
            this.testASAPOutputToBTInput();
        }
    }
}
