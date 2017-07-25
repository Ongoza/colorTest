package com.ongoza.pcycolortest;

import android.util.Log;

/**
 * Utility class for scripts.
 * 
 * public members in this class can be accessed in scripts
 * after adding as a global variable.
 *
 * <pre>
 * GVRScriptManager sm = getGVRContext().getScriptManager();
 *
 * sm.addVariable("utils", new ScriptUtils());
 * </pre>
 */
public class ScriptUtils {
	private static final String TAG = Main.getTAG();

	public void log(String msg) {
		Log.d(TAG, msg);
	}
}