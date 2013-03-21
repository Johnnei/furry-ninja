package engine;

import java.util.ArrayList;

public class GameKeyboard {
	
	private static GameKeyboard instance = new GameKeyboard();
	public static GameKeyboard getInstance() {
		return instance;
	}
	
	/**
	 * All the keys which should be tracked
	 */
	private ArrayList<KeyboardKey> keyList;
	
	private GameKeyboard() {
		keyList = new ArrayList<>();
	}
	
	public void trackKey(int keyId) {
		KeyboardKey key = new KeyboardKey(keyId);
		if(!keyList.contains(key)) {
			keyList.add(key);
		}
	}
	
	public boolean isKeyPressed(int keyId) {
		return isKeyPressed(keyId, true);
	}
	
	public boolean isKeyPressed(int keyId, boolean consumePress) {
		KeyboardKey key = new KeyboardKey(keyId);
		if(keyList.contains(key)) {
			key = keyList.get(keyList.indexOf(key));
			return key.isPressed(consumePress);
		} else {
			return false;
		}
	}
	
	/**
	 * Updates all information for the game keyboard
	 */
	public void poll() {
		for(KeyboardKey key : keyList) {
			key.poll();
		}
	}

}
