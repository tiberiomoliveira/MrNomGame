package com.game.mrnomgame.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.game.mrnomgame.framework.Audio;
import com.game.mrnomgame.framework.Music;
import com.game.mrnomgame.framework.Sound;

public class AndroidAudio implements Audio {
	AssetManager assets;
	SoundPool    sound_poll;
	
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets     = activity.getAssets();
		this.sound_poll = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); 
	}
	
	@Override
	public Music newMusic(String filename) {
		try {
			AssetFileDescriptor asset_descriptor = assets.openFd(filename);
			return new AndroidMusic(asset_descriptor);
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}
	
	@Override
	public Sound newSound(String filename) {
		try {
			AssetFileDescriptor asset_descriptor = assets.openFd(filename);
			int sound_id = sound_poll.load(asset_descriptor, 0);
			return new AndroidSound(sound_poll, sound_id);
		}
		catch (IOException e) {
			throw new RuntimeException("Couldn't load sound '" + filename +"'");
		}
	}
}