package com.lostbits.tbd;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Eric Parsons
 * @version Final0
 *
 * Main application for game
 */

public class GameActivity extends Activity
{
    private tbdSurfaceView mGLView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int level = bundle.getInt("level");
        Log.d("Level", "LevelId:" + level);

        mGLView = new tbdSurfaceView(this,"/sdcard/MakeYourOwnSidescroller/level" + level + ".txt");
        mGLView.setSystemUiVisibility(GLSurfaceView.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(mGLView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        mGLView.onResume();
    }
}


