package com.game.mrnomgame.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.game.mrnomgame.framework.FileIO;

public class AndroidFileIO implements FileIO {
	Context      context;
	AssetManager assets;
	String       ext_storage_path;
	
	public AndroidFileIO(Context context) {
		this.context = context;
		this.assets  = context.getAssets();
		this.ext_storage_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
								File.separator;
	}
	
	@Override
	public InputStream readAsset(String filename) throws IOException {
		return assets.open(filename);
	}
	
	@Override
	public InputStream readFile(String filename) throws IOException {
		return new FileInputStream(ext_storage_path + filename);
	}
	
	@Override
	public OutputStream writeFile(String filename) throws IOException {
		return new FileOutputStream(ext_storage_path + filename);
	}
	
	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}