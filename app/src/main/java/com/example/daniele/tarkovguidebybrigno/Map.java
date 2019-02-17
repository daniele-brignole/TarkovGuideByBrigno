package com.example.daniele.tarkovguidebybrigno;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.*;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class Map extends ActionBarActivity {
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        final ImageView iv = (ImageView)findViewById(R.id.map);
        Intent i = getIntent();
        String select = i.getStringExtra("nome").toLowerCase();
        String uri = "@drawable/" +select;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        //Drawable dw = getResources().getDrawable(imageResource);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),imageResource,op);
        op.inSampleSize = calculateMaxScale(op.outHeight,op.outWidth);
        Log.d("Scale",""+op.inSampleSize);
        op.inJustDecodeBounds = false;
        op.inScaled = false;
        Bitmap bt = BitmapFactory.decodeResource(getResources(),imageResource,op);
        Log.d("Actual_height",""+bt.getHeight());
        iv.setImageBitmap(bt);
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()& MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        //Log.d("action_down", "mode=DRAG");
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        //Log.d("ACTION_POINTER_DOWN", "oldDist=" + oldDist);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                            //Log.d(TAG, "mode=ZOOM");
                        }
                        iv.setFocusable(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        //Log.d("ACTION_POINTER_UP", "mode=NONE");
                        iv.setFocusable(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY()
                                    - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                          // Log.d("ZOOM", "newDist=" + newDist);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }

                iv.setImageMatrix(matrix);

                return true;
            }

            });
        }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    private int calculateMaxScale(int height,int width){
        int maxScale = 1;
        boolean notMax = true;
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),id,options);*/
        while(notMax){
            height = height/maxScale;
            width = width/maxScale;
            Log.d("height",""+height);
            Log.d("width",""+width);
            if(height < 4096 && width < 4096){
                notMax = false;
            }
            else{
                maxScale*=2;
            }
        }
        return maxScale;
    }
}




