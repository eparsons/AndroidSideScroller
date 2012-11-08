package com.lostbits.tbd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @author Eric Parsons
 * @version Date: 4/27/12
 *          Time: 5:12 PM
 */
public class MainMenu extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainmenu);

        //Initialize buttons and register onClick handlers
        final Button level1Button = (Button) findViewById(R.id.button1);
        final Button level2Button = (Button) findViewById(R.id.button2);
        level1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("level", 1);
                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        level2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("level", 2);
                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}