package com.ds.mo.engine.glpacman;

import android.content.Context;
import android.util.Log;

import com.ds.mo.engine.android.GLGame;
import com.ds.mo.engine.android.Texture;
import com.ds.mo.engine.android.TextureRegion;
import com.ds.mo.engine.common.Animation;
import com.ds.mo.engine.common.Font;
import com.ds.mo.engine.framework.Music;
import com.ds.mo.engine.framework.Sound;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Pac-man assets
 * <p/>
 * Created by Mo on 31/05/2017.
 */
public class Assets {
    public static Texture pacSheet;
    public static TextureRegion whole;

    //Active tiles
    public static Animation pacman;
    public static Animation pacmanDeath;
    public static TextureRegion dot;
    public static TextureRegion energizer;
    public static Animation ghost;
    public static TextureRegion eyesU, eyesD, eyesL, eyesR;

    //Inactive tiles
    public static TextureRegion double_tl;
    public static TextureRegion double_tm;
    public static TextureRegion double_tr;
    public static TextureRegion double_ml;
    public static TextureRegion double_mr;
    public static TextureRegion double_bl;
    public static TextureRegion double_bm;
    public static TextureRegion double_br;

    public static TextureRegion line_tl;
    public static TextureRegion line_tm;
    public static TextureRegion line_tr;
    public static TextureRegion line_ml;
    public static TextureRegion line_mr;
    public static TextureRegion line_bl;
    public static TextureRegion line_bm;
    public static TextureRegion line_br;

    public static TextureRegion line_big_tl, line_big_tr, line_big_bl, line_big_br;

    public static TextureRegion hor_tl, hor_tr, hor_bl, hor_br;
    public static TextureRegion ver_tl, ver_tr, ver_bl, ver_br;

    public static TextureRegion square_tl, square_tr, square_bl, square_br;

    public static TextureRegion home_l, home_r, home_door;
    //------------------------------------------------------------------------

    //Primitive shapes
    public static TextureRegion fillRect;
    public static TextureRegion fillCircle;
    public static TextureRegion boundsCircle;
    public static TextureRegion boundsRect;
    //Game music
    public static Music music;
    public static Sound waka;
    public static Sound wa;
    public static Sound ka;
    public static Sound deathSfx;

    //Game font
//    public static Font numbers;
    public static Font font;

    //World file
    public static JSONObject pacWorld;
    public static JSONObject pacIntersection;

    public static void load(GLGame glGame) {
        Log.d("Assets", "Loading assets...");

        //Load Images
        pacSheet = new Texture(glGame, "pacSheet.png");
        whole = new TextureRegion(pacSheet, 0, 0, 64, 64);

        //Only gets the first row (looking right)
        pacman = new Animation(0.05f,
                new TextureRegion(pacSheet, 32 * 2, 0, 32, 32),
                new TextureRegion(pacSheet, 32 * 3, 0, 32, 32),
                new TextureRegion(pacSheet, 32 * 4, 0, 32, 32),
                new TextureRegion(pacSheet, 32 * 3, 0, 32, 32));
        pacmanDeath = new Animation(0.1f,
                new TextureRegion(pacSheet, 32 * 2, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 3, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 4, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 5, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 6, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 7, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 8, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 9, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 10, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 11, 32 * 2, 32, 32),
                new TextureRegion(pacSheet, 32 * 12, 32 * 2, 32, 32));
        dot = new TextureRegion(pacSheet, 8 * 6, 8 * 4, 8, 8);
        energizer = new TextureRegion(pacSheet, 8 * 7, 8 * 4, 8, 8);
        ghost = new Animation(0.1f,
                new TextureRegion(pacSheet, 32 * 6, 0, 32, 32),
                new TextureRegion(pacSheet, 32 * 7, 0, 32, 32));
        eyesR = new TextureRegion(pacSheet, 32 * 6, 32 * 1, 32, 32);
        eyesD = new TextureRegion(pacSheet, 32 * 7, 32 * 1, 32, 32);
        eyesL = new TextureRegion(pacSheet, 32 * 8, 32 * 1, 32, 32);
        eyesU = new TextureRegion(pacSheet, 32 * 9, 32 * 1, 32, 32);

        double_tl = new TextureRegion(pacSheet, 8 * 0, 0, 8, 8);
        double_tm = new TextureRegion(pacSheet, 8 * 1, 0, 8, 8);
        double_tr = new TextureRegion(pacSheet, 8 * 2, 0, 8, 8);
        double_ml = new TextureRegion(pacSheet, 8 * 0, 8 * 1, 8, 8);
        double_mr = new TextureRegion(pacSheet, 8 * 2, 8 * 1, 8, 8);
        double_bl = new TextureRegion(pacSheet, 8 * 0, 8 * 2, 8, 8);
        double_bm = new TextureRegion(pacSheet, 8 * 1, 8 * 2, 8, 8);
        double_br = new TextureRegion(pacSheet, 8 * 2, 8 * 2, 8, 8);

        line_tl = new TextureRegion(pacSheet, 8 * 3, 0, 8, 8);
        line_tm = new TextureRegion(pacSheet, 8 * 4, 0, 8, 8);
        line_tr = new TextureRegion(pacSheet, 8 * 5, 0, 8, 8);
        line_ml = new TextureRegion(pacSheet, 8 * 3, 8 * 1, 8, 8);
        line_mr = new TextureRegion(pacSheet, 8 * 5, 8 * 1, 8, 8);
        line_bl = new TextureRegion(pacSheet, 8 * 3, 8 * 2, 8, 8);
        line_bm = new TextureRegion(pacSheet, 8 * 4, 8 * 2, 8, 8);
        line_br = new TextureRegion(pacSheet, 8 * 5, 8 * 2, 8, 8);

        line_big_tl = new TextureRegion(pacSheet, 8 * 6, 0, 8, 8);
        line_big_tr = new TextureRegion(pacSheet, 8 * 7, 0, 8, 8);
        line_big_bl = new TextureRegion(pacSheet, 8 * 6, 8 * 1, 8, 8);
        line_big_br = new TextureRegion(pacSheet, 8 * 7, 8 * 1, 8, 8);

        hor_tl = new TextureRegion(pacSheet, 8 * 1, 8 * 5, 8, 8);
        hor_tr = new TextureRegion(pacSheet, 8 * 0, 8 * 5, 8, 8);
        hor_bl = new TextureRegion(pacSheet, 8 * 1, 8 * 4, 8, 8);
        hor_br = new TextureRegion(pacSheet, 8 * 0, 8 * 4, 8, 8);

        ver_tl = new TextureRegion(pacSheet, 8 * 3, 8 * 5, 8, 8);
        ver_tr = new TextureRegion(pacSheet, 8 * 2, 8 * 5, 8, 8);
        ver_bl = new TextureRegion(pacSheet, 8 * 3, 8 * 4, 8, 8);
        ver_br = new TextureRegion(pacSheet, 8 * 2, 8 * 4, 8, 8);

        square_tl = new TextureRegion(pacSheet, 8 * 4, 8 * 4, 8, 8);
        square_tr = new TextureRegion(pacSheet, 8 * 5, 8 * 4, 8, 8);
        square_bl = new TextureRegion(pacSheet, 8 * 4, 8 * 5, 8, 8);
        square_br = new TextureRegion(pacSheet, 8 * 5, 8 * 5, 8, 8);

        home_l = new TextureRegion(pacSheet, 8 * 4, 8 * 3, 8, 8);
        home_r = new TextureRegion(pacSheet, 8 * 5, 8 * 3, 8, 8);
        home_door = new TextureRegion(pacSheet, 8 * 6, 8 * 2, 8, 8);

        font = new Font(pacSheet, 0, 32 * 4, 8, 16, 16);

        //Load world map
        Log.d("Assets", "trying to load json file...");
        String world = loadJson(glGame, "pacworld.json");
        try {
//            glGame.getFileIO().readAsset("pacworld.json");
            pacWorld = new JSONObject(world);
            Log.d("Assets", "pacman WORLD json loaded...");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String intersection = loadJson(glGame, "pacmanIntersection.json");
        try {
            pacIntersection = new JSONObject(intersection);
            Log.d("Assets", "pacman INTERSECTION json loaded...");
        } catch (JSONException e){
            e.printStackTrace();
        }

        //Load SFX
        waka = glGame.getAudio().newSound("sfx/waka_waka.wav");
        wa = glGame.getAudio().newSound("sfx/wa.wav");
        ka = glGame.getAudio().newSound("sfx/ka.wav");
        deathSfx = glGame.getAudio().newSound("sfx/deathSfx.wav");
        //Load Audio
        music = glGame.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        if (Settings.soundEnabled) {
            music.play();
        }
    }

    /**
     * Reloads all Textures and plays music when app resumes
     */
    public static void reload() {
        //Reload textures
        pacSheet.reload();

        //------------------------------------------------------------
        //Play background music
        if (Settings.soundEnabled) {
            music.play();
        }
    }

    public static void playSound(Sound sound) {
        if (Settings.soundEnabled) {
            sound.play(1);
        }
    }

    private static String loadJson(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
