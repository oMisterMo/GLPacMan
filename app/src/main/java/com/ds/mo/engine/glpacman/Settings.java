package com.ds.mo.engine.glpacman;

import android.util.Log;

import com.ds.mo.engine.framework.FileIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Stores basic settings for the game
 * <p>
 * Created by Mo on 01/06/2017.
 */

public class Settings {
    public static boolean soundEnabled = true;
    public final static int[] highScores = new int[]{100, 80, 50, 30, 10};
    public final static String file = ".glclonewars";

    /**
     * Loads from the SD card a file called .glclonewars
     *
     * @param files file handler
     */
    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            Log.d("Settings", "loading game......");
            //Initialise input stream
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));

            //Read each line and store
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < highScores.length; i++) {
                highScores[i] = Integer.parseInt(in.readLine());
            }
            Log.d("Settings", "loaded game......");
        } catch (IOException e) {
            //If we can't read a file, default settings will be used
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.newLine();
            Log.d("Settings", "Writing: " + Boolean.toString(soundEnabled));
            for (int i = 0; i < highScores.length; i++) {
                out.write(Integer.toString(highScores[i]));
                out.newLine();
            }
            //debug
            for (int i = 0; i < highScores.length; i++) {
                Log.d("Settings", "Writing: " + Integer.toString(highScores[i]));
            }
            Log.d("Settings", "***Saved game***");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < highScores.length; i++) {
            if (highScores[i] < score) {
                for (int j = highScores.length - 1; j > i; j--) {
                    highScores[j] = highScores[j - 1];
                }
                highScores[i] = score;
                break;
            }
        }
    }
}
