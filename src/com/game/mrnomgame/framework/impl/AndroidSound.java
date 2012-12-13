package com.game.mrnomgame.framework.impl;

import android.media.SoundPool;

import com.game.mrnomgame.framework.Sound;

public class AndroidSound implements Sound {
	SoundPool sound_pool;
	int       sound_id;
	
	public AndroidSound(SoundPool sound_pool, int sound_id) {
		this.sound_pool = sound_pool;
		this.sound_id   = sound_id;
	}
	
	@Override
	public void play(float volume) {
		sound_pool.play(sound_id, volume, volume, 0, 0, 1);
	}
	
	@Override
	public void dispose() {
		sound_pool.unload(sound_id);
	}
}