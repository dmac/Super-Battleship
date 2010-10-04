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
	public  static final int OUTER_PADDING = 5;

	public static int SHIP_START_Y;
	public static int CARRIER_START_X;
	public static int BATTLESHIP_START_X;
	public static int CRUISER_START_X;
	public static int SUBMARINE_START_X;
	public static int DESTROYER_START_X;

	private float cellSize;
	private Paint paint;
	private Rect border;
	private final Context context;

	private Ship ships[] = new Ship[5];
	private Ship carrier;
	private Ship battleship;
	private Ship cruiser;
	private Ship submarine;
	private Ship destroyer;
	private Ship draggingShip = null;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < ships.length; i++) {
				if (ships[i].containsPoint((int)event.getX(), (int)event.getY())) {
					draggingShip = ships[i];
					break;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (draggingShip != null) {
				draggingShip.setX((int)(event.getX() - draggingShip.getWidth() / 2));
				draggingShip.setY((int)(event.getY() - draggingShip.getHeight() / 2));
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if (draggingShip != null) {
				draggingShip.snapToGrid();
				draggingShip = null;
				invalidate();
			}
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
		calculateInitialShipPlacements();
		resetShips();
		super.onSizeChanged(w, h, oldw, oldh);
	};

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(border, paint);
		drawGridLines(canvas);
		for (Ship ship : ships) {
			ship.draw(canvas);
		}
	}

	private void resetShips() {
		carrier = new Ship(context, this, Ship.ShipType.CARRIER);
		battleship = new Ship(context, this, Ship.ShipType.BATTLESHIP);
		cruiser = new Ship(context, this, Ship.ShipType.CRUISER);
		submarine = new Ship(context, this, Ship.ShipType.SUBMARINE);
		destroyer = new Ship(context, this, Ship.ShipType.DESTROYER);
		ships[0] = carrier;
		ships[1] = battleship;
		ships[2] = cruiser;
		ships[3] = submarine;
		ships[4] = destroyer;
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

	private void calculateInitialShipPlacements() {
		SHIP_START_Y = (int) (OUTER_PADDING * 2 + cellSize * 10);
		CARRIER_START_X = (OUTER_PADDING);
		BATTLESHIP_START_X = (int) (OUTER_PADDING + 2 * cellSize);
		CRUISER_START_X = (int) (OUTER_PADDING + cellSize * 4);
		SUBMARINE_START_X = (int) (OUTER_PADDING + cellSize * 6);
		DESTROYER_START_X = (int) (OUTER_PADDING + cellSize * 8);
	}

	public float getCellSize() {
		return cellSize;
	}
}
