package com.kodorobo.superbattleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BattleshipGridView extends View {

	private final int OUTER_PADDING = 5;
	private final Paint paint;
	private final Rect border;

	public BattleshipGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		border = new Rect(OUTER_PADDING, OUTER_PADDING, 0, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		border.set(border.left, border.top,
				canvas.getWidth() - OUTER_PADDING, canvas.getWidth() - OUTER_PADDING);
		canvas.drawRect(border, paint);
		drawGridLines(canvas);
	}

	private void drawGridLines(Canvas canvas) {
		float cellSize = border.width()/10;
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
