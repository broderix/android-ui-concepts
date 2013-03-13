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

package ru.monkeycode.android.ui.login;

import java.util.ArrayList;

import ru.monkeycode.android.ui.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author broderix
 *
 */
public class LoginUI {
	private Animation mScale1Animation, mScale2Animation, mFade1Animation,
			mFade2Animation;
	private boolean mButtonEnterPressed = false;
	private ImageView mLoginImageView, mCloseImageView;
	private TextView mEnterTextView;
	private Context mContext;
	private RelativeLayout mRelativeLayoutContainer;
	private ArrayList<LoginUIButton> mButtons;
	private int mShiftLeft, mShiftTop;

	public class LoginUIButton {
		String name;
		int resourceId;
		Handler onClickHandler;
		ImageView view;

		public LoginUIButton(String name, int resourceId, Handler handler,
				ImageView view) {
			this.name = name;
			this.resourceId = resourceId;
			this.onClickHandler = handler;
			this.view = view;
		}

		public void handle() {
			onClickHandler.sendEmptyMessage(0);
		}
	}

	public LoginUI(Context context) {
		init(context);
	}

	public void addButton(String name, int resource, Handler handler) {
		mButtons.add(new LoginUIButton(name, resource, handler, null));
	}

	public boolean isPressed() {
		return mButtonEnterPressed;
	}

	/**
	 * call before draw()
	 */
	public void setPressed(boolean pressed) {
		mButtonEnterPressed = pressed;
	}

	/**
	 * Not tested but should work
	 * @param state View.VISIBLE|View.INVISIBLE|View.GONE
	 */
	public void setVisbility(int state) {
		if (mButtonEnterPressed) {
			setLoginButtonsVisibility(state);
			mCloseImageView.setVisibility(state);
		} else {
			mLoginImageView.setVisibility(state);
		}
	}
	
	public void draw() {
		int buttonsLength = mButtons.size();
		int i = 0;
		for (LoginUIButton button : mButtons) {
			int r = Math.round(mLoginImageView.getLayoutParams().width / 2);
			BitmapFactory.Options options = new BitmapFactory.Options();
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
					button.resourceId, options);
			int widthButton = bmp.getWidth();
			int heightButton = bmp.getHeight();
			int shiftCorrect = Math.round(widthButton / 2);
			ImageView view = new ImageView(mContext);
			view.setBackgroundResource(button.resourceId);
			LayoutParams fparams = new RelativeLayout.LayoutParams(widthButton,
					heightButton);
			fparams.addRule(RelativeLayout.ALIGN_LEFT, R.id.ui_login);
			fparams.addRule(RelativeLayout.ALIGN_TOP, R.id.ui_login);
			fparams.setMargins(
					(int) Math.round(mShiftLeft - shiftCorrect
							+ Math.sin(2 * Math.PI / buttonsLength * i)
							* (r - shiftCorrect)),
					(int) Math.round(mShiftTop - shiftCorrect
							+ Math.cos(2 * Math.PI / buttonsLength * i)
							* (r - shiftCorrect)), 0, 0);
			view.setLayoutParams(fparams);
			view.setVisibility(View.INVISIBLE);
			view.setTag(button);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doEnterButton();
					((LoginUIButton) v.getTag()).handle();
				}
			});
			mRelativeLayoutContainer.addView(view, fparams);
			button.view = view;
			i++;
		}
		if (mButtonEnterPressed) {
			setLoginButtonsVisibility(View.VISIBLE);
			mCloseImageView.setVisibility(View.VISIBLE);
			mLoginImageView.setVisibility(View.INVISIBLE);
			mEnterTextView.setVisibility(View.INVISIBLE);
			bringToFrontLoginButtons();
		} else {
			setLoginButtonsVisibility(View.INVISIBLE);
			mCloseImageView.setVisibility(View.INVISIBLE);
			mLoginImageView.setVisibility(View.VISIBLE);
			mEnterTextView.setVisibility(View.VISIBLE);
			bringToFrontLogin();
		}
	}

	public boolean shouldProcessBack() {
		if (mButtonEnterPressed) {
			doEnterButton();
			return true;
		}
		return false;
	}

	/**
	 * !!!Not tested, but should work to redraw all buttons you should delete
	 * them all like mRelativeLayoutContainer.removeAllViews() and call draw()
	 * method
	 * 
	 * @param name
	 */
	public void removeButton(String name) {
		for (LoginUIButton button : mButtons) {
			if (button.view != null && name == button.name) {
				mRelativeLayoutContainer.removeView(button.view);
				break;
			}
		}
	}

	private void init(Context context) {
		mButtons = new ArrayList<LoginUIButton>();
		mContext = context;
		mFade1Animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_1);
		mFade2Animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_2);
		mLoginImageView = (ImageView) ((Activity) mContext)
				.findViewById(R.id.ui_login);
		mRelativeLayoutContainer = (RelativeLayout) ((Activity) mContext)
				.findViewById(R.id.ui_login_container);
		mShiftLeft = Math.round(mLoginImageView.getLayoutParams().width / 2);
		mShiftTop = Math.round(mLoginImageView.getLayoutParams().height / 2);
		mEnterTextView = (TextView) ((Activity) mContext)
				.findViewById(R.id.ui_enter_text);
		mCloseImageView = (ImageView) ((Activity) mContext)
				.findViewById(R.id.ui_login_close);

		mScale1Animation = AnimationUtils.loadAnimation(mContext,
				R.anim.scale_1);
		mScale1Animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mEnterTextView.setVisibility(View.INVISIBLE);
				bringToFrontLoginButtons();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mCloseImageView.setVisibility(View.VISIBLE);
				setLoginButtonsVisibility(View.VISIBLE);
			}
		});
		mScale2Animation = AnimationUtils.loadAnimation(mContext,
				R.anim.scale_2);
		mScale2Animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				mCloseImageView.setVisibility(View.GONE);
				setLoginButtonsVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				bringToFrontLogin();
				mEnterTextView.setVisibility(View.VISIBLE);
			}
		});

		mLoginImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mButtonEnterPressed) {
					doCloseButton();
				}
			}
		});
		mCloseImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doEnterButton();
			}
		});
		
	}

	private void doCloseButton() {
		mLoginImageView.startAnimation(mScale1Animation);
		startAnimationLoginButtons(mFade1Animation);
		mButtonEnterPressed = true;
	}

	private void doEnterButton() {
		mLoginImageView.startAnimation(mScale2Animation);
		startAnimationLoginButtons(mFade2Animation);
		mButtonEnterPressed = false;
	}

	private void startAnimationLoginButtons(Animation anim) {
		for (LoginUIButton button : mButtons) {
			if (button.view != null) {
				button.view.startAnimation(anim);
			}
		}
	}

	private void setLoginButtonsVisibility(int state) {
		for (LoginUIButton button : mButtons) {
			if (button.view != null) {
				button.view.setVisibility(state);
			}
		}
	}

	private void bringToFrontLoginButtons() {
		for (LoginUIButton button : mButtons) {
			if (button.view != null) {
				button.view.bringToFront();
			}
		}
	}

	private void bringToFrontLogin() {
		mLoginImageView.bringToFront();
		mEnterTextView.bringToFront();
		mCloseImageView.bringToFront();
	}
}
