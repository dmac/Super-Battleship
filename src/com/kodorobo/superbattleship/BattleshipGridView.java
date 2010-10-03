package com.kodorobo.superbattleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BattleshipGridView extends View {

	private final String TAG = "Battleship";

	public static final int SHIP_START_Y = 330;
	public static final int CRUISER_START_X = 30;

	private float cellSize;
	private final int OUTER_PADDING = 5;
	private Paint paint;
	private Rect border;
	private final Context context;

	private Ship ships[] = new Ship[1];
	private Ship cruiser;
	private Ship draggingShip = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "touch event down");
			for (int i = 0; i < ships.length; i++) {
				if (ships[i].containsPoint((int)event.getX(), (int)event.getY())) {
					draggingShip = ships[i];
					break;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "touch event move");
			if (draggingShip != null) {
				draggingShip.setX((int)(event.getX() - draggingShip.getWidth() / 2));
				draggingShip.setY((int)(event.getY() - draggingShip.getHeight() / 2));
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "touch event up");
			draggingShip = null;
			break;
		}
		return true;
	}

	public BattleshipGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.border = new Rect(OUTER_PADDING, OUTER_PADDING, w - OUTER_PADDING, w - OUTER_PADDING);
		this.cellSize = (float) (border.width() / 10.0);
		resetShips();
		super.onSizeChanged(w, h, oldw, oldh);
	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(border, paint);
		drawGridLines(canvas);
		cruiser.draw(canvas);
	}

	private void resetShips() {
		cruiser = new Ship(context, Ship.ShipType.CRUISER, cellSize);
		ships[0] = cruiser;
	}

	private void drawGridLines(Canvas canvas) {
		for (int col = 1; col < 10; col++) {
			canvas.drawLine(OUTER_PADDING + cellSize * col, OUTER_PADDING,
					OUTER_PADDING + cellSize * col, OUTER_PADDING + border.height(), paint);
		}
		for (int row = 1; row < 10; row++) {
			canvas.drawLine(OUTER_PADDING, OUTER_PADDING + cellSize * row,
					OUTER_PADDING + border.height(), OUTER_PADDING + cellSize * row, paint);
		}
	}
}
