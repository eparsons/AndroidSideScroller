package com.lostbits.tbd;

import android.graphics.Color;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric Parsons
 * @version Final1
 *
 * Date: 4/12/12
 * Time: 7:54 PM
 */
public class World{
    public final static float BLOCK_SIZE = 0.25f;
    private tbdGLES10Renderer renderer = null;
    private String worldFile = "";

    // String representation
    private String[] layoutStrings = {
            "G                                                                 G",
            "G                                                                 G",
            "G                                                                 G",
            "G                                                                 G",
            "G                                                                 G",
            "G                                                                 G",
            "G                            GGGGGG                               G",
            "G          P                                G e G      GG         G",
            "G   e                  e               e    GGGGG       e         G",
            "Ge                                                                G",
            "G          G           GGGG        eeeeeeee                       G",
            "G                GGG                                              G",
            "G    GG       G                                                   G",
            "GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG",
    };


    private ArrayList<Drawable> world = new ArrayList<Drawable>();
    private ArrayList<Drawable> enemies = new ArrayList<Drawable>();
    private Player player = new Player();
    private boolean loaded = false;

    /**
     * Constructor.  Stores reference to renderer
     * @param renderer
     */
    public World(tbdGLES10Renderer renderer){
        this.renderer = renderer;
        //loadWorld("/sdcard/MakeYourOwnSidescroller/level1.txt");
    }

    /**
     * Constructor.  Stores reference to renderer
     * @param renderer
     */
    public World(tbdGLES10Renderer renderer, String worldFile){
        this.renderer = renderer;
        loadWorld(worldFile);
    }

    /**
     * Returns the drawables that make up the world environment
     *
     * @return ArrayList<Drawable> of world drawables
     */
    public ArrayList<Drawable> getWorldDrawables() {
        return world;
    }

    /**
     * Returns the enemies of the current world
     *
     * @return ArrayList<Drawable> of enemies
     */
    public ArrayList<Drawable> getEnemies() {
        return enemies;
    }

    /**
     * Provides a reference to the current player
     *
     * @return Player for current world
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Indicates whether the current world has finished loading
     *
     * @return boolean
     */
    public boolean isLoaded(){
        return loaded;
    }

    /**
     * Returns the ModelView x position of a drawable given it's string x position
     *
     * @param x int X position of drawable in string representation of world
     * @return float x position in ModelView Coordinates
     */
    private float xToMVCoords(int x){
        return BLOCK_SIZE*x;
    }

    /**
     * Returns the ModelView y position of a drawable given it's string y position
     *
     * @param y int Y position of drawable in string representation of world
     * @return float y position in ModelView Coordinates
     */
    private float yToMVCoords(int y){
        return BLOCK_SIZE*y;
    }

    /**
     * Reads string array and generates the drawables in the world
     */
    private void loadWorld(String worldFile){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(worldFile));
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
            layoutStrings = lines.toArray(new String[lines.size()]);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //Each character represents a .5 by .5 square area
        int height = layoutStrings.length;
        for(int y = 0; y < height; y++){

            char[] rowChars = layoutStrings[y].toCharArray();
            for(int x = 0; x <rowChars.length; x++){
                if(rowChars[x] == 'G'){
                    //Generating ground blocks
                    Ground ground = new Ground();
                    ground.initDrawable(renderer);
                    ground.setColor(Color.argb(224, 160, 82, 45));
                    ground.setScale(0.5f);
                    ground.setPositionX( xToMVCoords(x) + ground.getWidth()/2);
                    ground.setPositionY( yToMVCoords(height - 1 - y) + ground.getHeight()/2);
                    ground.setTexture(R.drawable.block);
                    world.add(ground);
                } else if(rowChars[x] == 'e'){
                    //Generating enemies
                    Pacer pacer = new Pacer();
                    pacer.initDrawable(renderer);
                    pacer.setDx(0.01f);
                    pacer.setScale(0.5f);
                    pacer.setPositionX( xToMVCoords(x) + pacer.getWidth()/2);
                    pacer.setPositionY( yToMVCoords(height - 1 - y) + pacer.getHeight()/2);
                    pacer.setTexture(R.drawable.pacer);
                    enemies.add(pacer);
                } else if(rowChars[x] == 'P'){
                    //Generating player
                    player = new Player();
                    player.setWidth(.7f*player.getWidth());
                    player.initDrawable(renderer);
                    player.setScale(0.65f);
                    player.setPositionX( xToMVCoords(x) + player.getWidth()/2 );
                    player.setPositionY( yToMVCoords(height - 1 - y) + player.getHeight()/2);
                    player.setTexture(R.drawable.stickman);
                }
            }
        }

        loaded = true;
    }

    /**
     * Checks for collisions between drawables in the world
     */
    public void checkCollisions(){
        checkObjectCollisions(player, world);
        for(Drawable enemy : enemies){
            checkObjectCollisions(enemy, world);
        }
        checkObjectCollisions(player, enemies);
    }

    /**
     * Given a GameEntity, determines if they've collided with another object in the world
     * @param dynamicDrawable
     */
    private void checkObjectCollisions(Drawable dynamicDrawable, ArrayList<Drawable> otherObjects){
        GameEntity actor = (GameEntity)dynamicDrawable;

        for(Drawable otherObject : otherObjects){
            if(actor.collision(otherObject) && actor.isInPlay()){

                if(actor.collisionBottom(otherObject)){
                    actor.handleBottomCollision(otherObject);
                    //Log.d("World", "Bottom collision");
                }
                if(actor.collisionTop(otherObject)){
                    actor.handleTopCollision(otherObject);
                    //Log.d("World", "Top collision");
                }
                if(actor.collisionLeft(otherObject)){
                    actor.handleLeftCollision(otherObject);
                    //Log.d("World", "Left collision");
                }
                if(actor.collisionRight(otherObject)){
                    actor.handleRightCollision(otherObject);
                    //Log.d("World", "Right collision");
                }
            }
        }
    }
}
