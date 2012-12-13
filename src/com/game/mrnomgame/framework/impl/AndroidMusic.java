package com.game.mrnomgame.framework.impl;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.game.mrnomgame.framework.Music;

public class AndroidMusic implements Music, OnCompletionListener {
	MediaPlayer media_player;
	boolean     is_prepared = false;
	
	public AndroidMusic(AssetFileDescriptor asset_descriptor) {
		media_player = new MediaPlayer();
		
		try {
			media_player.setDataSource(asset_descriptor.getFileDescriptor(),
									   asset_descriptor.getStartOffset(),
									   asset_descriptor.getLength());
			media_player.prepare();
			is_prepared = true;
			media_player.setOnCompletionListener(this);			
		}
		catch (Exception e) {
			is_prepared = false;
			throw new RuntimeException("Couldn't load music.");
		}
	}
	
	@Override
	public void dispose() {
		if (media_player.isPlaying())
			media_player.stop();
		media_player.release();
	}
	
	@Override
	public boolean isLooping() {
		return media_player.isLooping();
	}
	
	@Override
	public boolean isPlaying() {
		return media_player.isPlaying();
	}
	
	@Override
	public boolean isStopped() {
		return !is_prepared;
	}
	
	@Override
	public void pause() {
		if (media_player.isPlaying())
			media_player.pause();
	}
	
	@Override
	public void play() {
		if (media_player.isPlaying())
			return;
		try {
			synchronized (this) {
				if (!is_prepared)
					media_player.prepare();
				media_player.start();
			}
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setLooping(boolean is_looping) {
		media_player.setLooping(is_looping);
	}
	
	@Override
	public void setVolume(float volume) {
		media_player.setVolume(volume, volume);
	}
	
	@Override
	public void stop() {
		media_player.stop();
		synchronized (this) {
			is_prepared = false;
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer player) {
		synchronized (this) {
			is_prepared = false;			
		}
	}
}