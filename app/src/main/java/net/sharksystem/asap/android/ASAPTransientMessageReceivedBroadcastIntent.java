package net.sharksystem.asap.android;

import android.content.Intent;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.utils.ASAPSerialization;
import net.sharksystem.utils.Log;

import java.io.IOException;

public class ASAPTransientMessageReceivedBroadcastIntent extends Intent {

    private CharSequence format;
    private CharSequence uri;
    private byte[] content;
    private ASAPHop asapHop;

    public ASAPTransientMessageReceivedBroadcastIntent(
            CharSequence format, CharSequence uri, byte[] content, ASAPHop asapHop
    ) throws ASAPException {
        if(format == null || uri == null || content == null || asapHop == null)
            throw new ASAPException("parameters must no be null");

        this.setAction(ASAPAndroid.ASAP_TRANSIENT_MESSAGE_RECEIVED_ACTION);

        this.putExtra(ASAPServiceMethods.FORMAT_TAG, format);
        this.putExtra(ASAPServiceMethods.URI_TAG, uri);
        this.putExtra(ASAPAndroid.CONTENT, content);
        try {
            byte[] asapHopBytes = ASAPSerialization.asapHop2ByteArray(asapHop);
            this.putExtra(ASAPAndroid.ASAP_HOPS, asapHopBytes);
        }catch (IOException e){
            // ignore
            Log.writeLogErr(this, "cannot serialize ASAPHop: " + asapHop);
        }

        this.format = format;
        this.uri = uri;
        this.content = content;
        this.asapHop = asapHop;
    }

    public ASAPTransientMessageReceivedBroadcastIntent(Intent intent) throws IOException, ASAPException {
        this.format = intent.getStringExtra(ASAPServiceMethods.FORMAT_TAG);
        this.uri = intent.getStringExtra(ASAPServiceMethods.URI_TAG);
        this.content = intent.getByteArrayExtra(ASAPAndroid.CONTENT);
        byte[] asapHopBytes = intent.getByteArrayExtra(ASAPAndroid.ASAP_HOPS);
        this.asapHop = ASAPSerialization.byteArray2ASAPHop(asapHopBytes);
    }

    public CharSequence getFormat() {
        return format;
    }

    public CharSequence getUri() {
        return uri;
    }

    public byte[] getContent() {
        return content;
    }

    public ASAPHop getAsapHop() {
        return asapHop;
    }
}

