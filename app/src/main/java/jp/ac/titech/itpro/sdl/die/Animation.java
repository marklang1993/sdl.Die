package jp.ac.titech.itpro.sdl.die;

import android.widget.SeekBar;

import java.util.TimerTask;

public class Animation extends TimerTask {

    private SeekBar seekBarX;
    private SeekBar seekBarY;
    private SeekBar seekBarZ;

    Animation(SeekBar x, SeekBar y, SeekBar z){
        seekBarX = x;
        seekBarY = y;
        seekBarZ = z;
    }

    @Override
    public void run() {
        int currentX = seekBarX.getProgress();
        int currentY = seekBarY.getProgress();
        int currentZ = seekBarZ.getProgress();

        seekBarX.setProgress((currentX + 1) % 360);
        seekBarY.setProgress((currentY + 1) % 360);
        seekBarZ.setProgress((currentZ + 1) % 360);
    }
}
