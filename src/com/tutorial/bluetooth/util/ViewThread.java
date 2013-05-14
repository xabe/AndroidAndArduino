package com.tutorial.bluetooth.util;

import android.graphics.Canvas;
import android.os.Process;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tutorial.bluetooth.view.BaseView;

/**
 * Clase que gentiona el ciclo de dibujar por pantalla
 * @author Chabir Atrahouch
 *
 */
public class ViewThread extends Thread {
	private static final String TAG = ViewThread.class.getSimpleName();
	private SurfaceHolder surfaceHolder;
	private BaseView baseView;
	private boolean running;
	private int mode;

	public ViewThread(SurfaceHolder surfaceHolder, BaseView baseView) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.baseView = baseView;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void doStart() {
		synchronized (surfaceHolder) {
			setState(Constants.STATE_RUNNING);
		}
	}

	public void pause() {
		synchronized (surfaceHolder) {
			if (mode == Constants.STATE_RUNNING)
				setState(Constants.STATE_PAUSE);
		}
	}

	public void setState(int mode) {
		synchronized (surfaceHolder) {
			setState(mode, null);
		}
	}

	public void setState(int mode, CharSequence message) {
		synchronized (surfaceHolder) {
			this.mode = mode;
		}
	}

	public void unpause() {
		synchronized (surfaceHolder) {
			setState(Constants.STATE_RUNNING);
		}
	}

	public int getMode() {
		synchronized (surfaceHolder) {
			return mode;
		}
	}

	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
		Canvas canvas;
		Log.d(TAG, "Starting game loop");

		long beginTime;
		long timeDiff;
		int sleepTime;
		int framesSkipped;

		sleepTime = 0;
		while (running) {
			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					if (mode == Constants.STATE_RUNNING) {
						this.baseView.update();
						this.baseView.draw(canvas);
					}
					timeDiff = System.currentTimeMillis() - beginTime;
					sleepTime = (int) (Constants.FRAME_PERIOD - timeDiff);
					if (sleepTime > 0) 
					{
						try 
						{
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}

					while (sleepTime < 0
							&& framesSkipped < Constants.MAX_FRAME_SKIPS
							&& mode == Constants.STATE_RUNNING) {
						this.baseView.update();
						sleepTime += Constants.FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

}
