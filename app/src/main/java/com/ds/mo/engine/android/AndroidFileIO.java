package com.ds.mo.engine.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import com.ds.mo.engine.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mo on 28/05/2017.
 */
public class AndroidFileIO implements FileIO {
    Context context;
    AssetManager assetManager;
    String externalStorage;

    public AndroidFileIO(Context context){
        this.context = context;
        this.assetManager = context.getAssets();
        this.externalStorage = context.getExternalFilesDir(null).getAbsolutePath() + File.separator;
    }
    @Override
    public InputStream readAsset(String filename) throws IOException {
        return assetManager.open(filename);
    }

    @Override
    public InputStream readFile(String filename) throws IOException {
        return new FileInputStream(externalStorage + filename);
    }

    @Override
    public OutputStream writeFile(String filename) throws IOException {
        return new FileOutputStream(externalStorage + filename);
    }

    public SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
