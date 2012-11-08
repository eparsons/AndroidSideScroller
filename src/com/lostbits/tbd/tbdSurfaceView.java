package com.lostbits.tbd;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Eric Parsons
 * Date: 4/3/12
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @author Eric Parsons
 * @version Final0
 *
 * Android View for game
 */
class tbdSurfaceView extends GLSurfaceView {
    private static final String TAG = "TDB_Surface_View";

    final public tbdGLES10Renderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;

    private Player player;

    /**
     * Constructor, stores reference to renderer
     * @param context context of the activity that created this view.
     */
    public tbdSurfaceView(Context context, String levelFile){
        super(context);
        mRenderer = new tbdGLES10Renderer(levelFile);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        mRenderer.setView(this);
    }

    /**
     * Adds reference to player in this view
     *
     * @param newPlayer Player
     */
    public void setPlayer(Player newPlayer){
        player = newPlayer;
    }

    /**
     * Handles touch events to GLSurfaceView
     *
     * @param e The event that is generated
     * @return True if event handled, false otherwise
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(player == null){
            Log.d("View", "Player null");
            return true;
        }

        if(!player.isInPlay())
            return true;

        boolean moveLeft = false;
        boolean moveRight = false;
        for (int i = 0; i < e.getPointerCount(); i++) {
            float x = e.getX(i);
            float y = e.getY(i);

            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    break;

                default:
                    //Log.d(TAG, "Pointer pressed: "+x + "  Action: " + e.getAction());
                    if(x < mRenderer.getWidth()*1/8){
                        moveLeft = true;
                    } else if (x >= mRenderer.getWidth()*1/8 && x <  mRenderer.getWidth()*2/8) {
                        moveRight = true;
                    } else if(x > mRenderer.getWidth()*7/8) {
                        player.jump();
                    }
                    break;
            }

            if(moveLeft && !moveRight) {
                player.setDx(-player.RUN_SPEED);
                //Log.d(TAG, "MoveLeft: " + moveLeft + "   MoveRight: " + moveRight + "   Dx: " + player.getDx());
            } else if(!moveLeft && moveRight) {
                player.setDx(player.RUN_SPEED);
                //Log.d(TAG, "MoveLeft: " + moveLeft + "   MoveRight: " + moveRight + "   Dx: " + player.getDx());
            } else {
                player.setDx(0.0f);
                //Log.d(TAG, "MoveLeft: " + moveLeft + "   MoveRight: " + moveRight + "   Dx: " + player.getDx());
            }
        }

        return true;
    }



//    /** Show an event in the LogCat view, for debugging */
//    private void dumpEvent(MotionEvent event) {
//        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
//                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
//        StringBuilder sb = new StringBuilder();
//        int action = event.getAction();
//        int actionCode = action & MotionEvent.ACTION_MASK;
//        sb.append("event ACTION_" ).append(names[actionCode]);
//        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
//                || actionCode == MotionEvent.ACTION_POINTER_UP) {
//            sb.append("(pid " ).append(
//                    action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
//            sb.append(")" );
//        }
//        sb.append("[" );
//        for (int i = 0; i < event.getPointerCount(); i++) {
//            sb.append("#" ).append(i);
//            sb.append("(pid " ).append(event.getPointerId(i));
//            sb.append(")=" ).append((int) event.getX(i));
//            sb.append("," ).append((int) event.getY(i));
//            if (i + 1 < event.getPointerCount())
//                sb.append(";" );
//        }
//        sb.append("]" );
//        Log.d(TAG, sb.toString());
//    }
}