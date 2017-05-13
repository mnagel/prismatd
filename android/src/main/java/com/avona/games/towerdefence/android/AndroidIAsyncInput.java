package com.avona.games.towerdefence.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.avona.games.towerdefence.input.IAsyncInput;

// TODO FIX ... THIS
public class AndroidIAsyncInput implements IAsyncInput {

	private static final String TAG = "AndroidIAsyncInput";

	private final Context context;

	AndroidIAsyncInput(Context context) {
		this.context = context;
	}

	@Override
	public void chooseRunnable(final String title, final String[] options, final boolean isCancelAllowed, final MyRunnable callback) {
		final Handler guiMQ = new Handler(Looper.getMainLooper());
		final boolean guitarget = Looper.myLooper() == Looper.getMainLooper();

		Log.i(TAG, "posting to gui");
		guiMQ.post(new Runnable() {
			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(title);
				builder.setCancelable(isCancelAllowed);
				builder.setItems(options, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, final int which) {
						Log.i(TAG, "posting to back to gui");

						Runnable r = new Runnable() {
							@Override
							public void run() {
								callback.run(which);
							}
						};

						if (guitarget) {
							guiMQ.post(r);
						} else {
							Log.i(TAG, "posting to back to GameRenderProxy");
							GameRenderProxy.msgs.add(r);
						}
					}
				});
				builder.show();
			}
		});
	}
}
