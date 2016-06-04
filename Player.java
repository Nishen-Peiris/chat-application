/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author nishen
 */
public class Player {

    private AudioFormat audioFormat;
    private ByteArrayOutputStream byteArrayOutputStream;
    private SourceDataLine sourceDataLine;

    public Player() {
        try {
            audioFormat = getAudioFormat();     //get the audio format

            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            //Setting the maximum volume
            FloatControl control = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(control.getMaximum());
        } catch (LineUnavailableException ex) {
            System.out.println("Line unavailable.");
        }
    }

    public void play(byte[] tempBuffer) {
        byteArrayOutputStream = new ByteArrayOutputStream();

           // Play the record
            byteArrayOutputStream.write(tempBuffer, 0, tempBuffer.length);
            sourceDataLine.write(tempBuffer, 0, tempBuffer.length);
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
