/**
 * Copyright (c) 2013, Danilov Kirill (brody.broderix@gmail.com)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */
package com.example.androidtest;

import ru.monkeycode.android.ui.login.LoginUI;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

public class AndroidUIConceptsDemos extends Activity {

	private static final String TAG = AndroidUIConceptsDemos.class.getName();
	private LoginUI mLogin;
	private final String LOGIN_UI_PRESSED = "__login_ui_pressed";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		boolean pressed = false;
		if (savedInstanceState != null && savedInstanceState.containsKey(LOGIN_UI_PRESSED)) {
			pressed = savedInstanceState.getBoolean(LOGIN_UI_PRESSED);
		}
		Log.i(TAG, "pressed="+pressed);
		mLogin = new LoginUI(this);
		mLogin.setPressed(pressed);
		mLogin.addButton("facebook", ru.monkeycode.android.ui.R.drawable.f_logo, new Handler(){
			@Override public void handleMessage(Message msg) {
				Toast.makeText(AndroidUIConceptsDemos.this, "FACEBOOK", Toast.LENGTH_SHORT).show();
			}
		});
		mLogin.addButton("google+", ru.monkeycode.android.ui.R.drawable.gplus_logo, new Handler(){
			@Override public void handleMessage(Message msg) {
				Toast.makeText(AndroidUIConceptsDemos.this, "GOOGLE+", Toast.LENGTH_SHORT).show();
			}
		});
		mLogin.addButton("twitter", ru.monkeycode.android.ui.R.drawable.t_logo, new Handler(){
			@Override public void handleMessage(Message msg) {
				Toast.makeText(AndroidUIConceptsDemos.this, "TWITTER", Toast.LENGTH_SHORT).show();
			}
		});
		mLogin.draw();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(LOGIN_UI_PRESSED, mLogin.isPressed());
		super.onSaveInstanceState(outState);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (mLogin.shouldProcessBack()) {
				return true;
			}
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
