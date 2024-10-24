package com.example.prn231;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MentorScheduleView extends View {
    private static final int DAYS_IN_WEEK = 7;
    private static final int SLOTS_PER_DAY = 4;
    private static final String[] DAYS = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private static final String[] TIME_SLOTS = {"9-11", "11-13", "14-16", "16-18"};

    private Paint textPaint;
    private Paint cellPaint;
    private float cellWidth;
    private float cellHeight;
    private boolean[][] isBooked = new boolean[DAYS_IN_WEEK][SLOTS_PER_DAY];

    public MentorScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        cellPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = (float) w / (DAYS_IN_WEEK + 1);
        cellHeight = (float) h / (SLOTS_PER_DAY + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw day headers
        for (int i = 0; i < DAYS.length; i++) {
            canvas.drawText(DAYS[i], (i + 1.5f) * cellWidth, cellHeight / 2, textPaint);
        }

        // Draw time slots and availability cells
        for (int slot = 0; slot < SLOTS_PER_DAY; slot++) {
            canvas.drawText(TIME_SLOTS[slot], cellWidth / 2, (slot + 1.5f) * cellHeight, textPaint);

            for (int day = 0; day < DAYS_IN_WEEK; day++) {
                cellPaint.setColor(isBooked[day][slot] ? Color.RED : Color.GREEN);
                canvas.drawRect(
                        (day + 1) * cellWidth,
                        (slot + 1) * cellHeight,
                        (day + 2) * cellWidth,
                        (slot + 2) * cellHeight,
                        cellPaint
                );

                String text = isBooked[day][slot] ? "Booked" : "Available";
                canvas.drawText(text, (day + 1.5f) * cellWidth, (slot + 1.5f) * cellHeight, textPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            int day = (int) (x / cellWidth) - 1;
            int slot = (int) (y / cellHeight) - 1;

            if (day >= 0 && day < DAYS_IN_WEEK && slot >= 0 && slot < SLOTS_PER_DAY) {
                if (!isBooked[day][slot]) {
                    isBooked[day][slot] = true;
                    invalidate();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // Method to set booking status (you'd call this with your actual booking data)
    public void setBookingStatus(int day, int slot, boolean booked) {
        if (day >= 0 && day < DAYS_IN_WEEK && slot >= 0 && slot < SLOTS_PER_DAY) {
            isBooked[day][slot] = booked;
            invalidate();
        }
    }
}
