package com.kodorobo.superbattleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class Ship {

	public enum ShipType {
		CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER
	}

	public enum Orientation {
		HORIZONTAL, VERTICAL
	}

	private int x;
	private int y;
	private int size;
	private ShipType type;
	private Orientation orientation;
	private BitmapDrawable bitmap_horizontal;
	private BitmapDrawable bitmap_vertical;
	private BattleshipGridView grid;
	private float cellSize;
	private Paint paint;
	private Context context;

	public Ship(Context context, BattleshipGridView grid, ShipType type) {
		this.context = context;
		this.type = type;
		this.grid = grid;
		this.cellSize = grid.getCellSize();
		this.paint = new Paint();
		switch (type) {
			case CARRIER: size = 5; break;
			case BATTLESHIP: size = 4; break;
			case CRUISER: size = 3; break;
			case SUBMARINE: size = 3; break;
			case DESTROYER: size = 2; break;
		}
		this.x = BattleshipGridView.CRUISER_START_X;
		this.y = BattleshipGridView.SHIP_START_Y;
		this.orientation = Orientation.VERTICAL;
		this.bitmap_horizontal = getBitmapForShip(type);
		this.bitmap_horizontal = scaleBitmapToGrid(bitmap_horizontal);
		this.bitmap_vertical = rotateBitmap(bitmap_horizontal);
	}

	public void draw(Canvas canvas) {
		BitmapDrawable bitmap = (BitmapDrawable) valueByOrientation(bitmap_horizontal, bitmap_vertical);
		canvas.drawBitmap(bitmap.getBitmap(), x, y, paint);
	}

	public boolean containsPoint(int x, int y) {
		return (x > this.x && x < this.x + getWidth() &&
			y > this.y && y < this.y + getHeight());
	}

	public void snapToGrid() {
		this.x = (int)(((x / (int)cellSize) * cellSize) + grid.getOuterPadding());
		this.y = (int)(((y / (int)cellSize) * cellSize) + grid.getOuterPadding());
	}

	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }

	public int getWidth() {
		return ((BitmapDrawable)valueByOrientation(bitmap_horizontal, bitmap_vertical))
			.getBitmap().getWidth();
	}

	public int getHeight() {
		return ((BitmapDrawable)valueByOrientation(bitmap_horizontal, bitmap_vertical))
			.getBitmap().getHeight();
	}

	private BitmapDrawable getBitmapForShip(ShipType type) {
		switch(type) {
		case CRUISER:
		default:
			return new BitmapDrawable(
					BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser));
		}
	}

	private BitmapDrawable rotateBitmap(BitmapDrawable bitmap) {
		Bitmap originalBitmap = bitmap.getBitmap();
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		Matrix m = new Matrix();
		m.postRotate(90);
		Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, m, true);
		return new BitmapDrawable(rotatedBitmap);
	}

	// Assumes we're dealing with the original, horizontal image.
	private BitmapDrawable scaleBitmapToGrid(BitmapDrawable bitmap) {
		Bitmap originalBitmap = bitmap.getBitmap();
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		float newWidth = cellSize * size;
		float newHeight = cellSize;
		Matrix m = new Matrix();
		m.postScale(newWidth / width, newHeight / height);
		Bitmap newBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, m, true);
		return new BitmapDrawable(newBitmap);
	}

	private Object valueByOrientation(Object ifHorizontal, Object ifVertical) {
		return (orientation == Orientation.HORIZONTAL ? ifHorizontal : ifVertical);
	}

	private int valueByOrientation(int ifHorizontal, int ifVertical) {
		return (orientation == Orientation.HORIZONTAL ? ifHorizontal : ifVertical);
	}

	private float valueByOrientation(float ifHorizontal, float ifVertical) {
		return (orientation == Orientation.HORIZONTAL ? ifHorizontal : ifVertical);
	}
}
