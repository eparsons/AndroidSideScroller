package com.lostbits.tbd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Eric Parsons
 * Date: 4/2/12
 * Time: 9:12 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @author Eric Parsons
 * @version Final0
 *
 * OpenGL mRenderer for game
 */

public class tbdGLES10Renderer implements GLSurfaceView.Renderer {
    private final int MAX_TEXTURES = 256;
    private String levelFile = "";

    private tbdSurfaceView view;
    private float eyeDistance = 5f;
    private float frustumStart = 3f;
    private float frustumEnd = 7f;
    private int width;
    private int height;
    private GL10 glRef;
    private World world = null;

    private HashMap<Integer, Integer> resourceToTextureIdMap = new HashMap<Integer, Integer>();
    private int[] textures = new int[MAX_TEXTURES];
    private int textureCount = 0;

    public tbdGLES10Renderer(String levelFile){
        super();
        this.levelFile = levelFile;
    }



    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Gets the right edge of the screen in Model View coordinates
     *
     * @return right edge of the screen in Model View coordinates
     */
    public float screenEdgeRight(){
        return ((float)width/(float)height)*(eyeDistance / frustumStart);
    }

    /**
     * Gets the left edge of the screen in Model View coordinates
     *
     * @return left edge of the screen in Model View coordinates
     */
    public float screenEdgeLeft(){
        return -1*((float)width/(float)height)*(eyeDistance / frustumStart);
    }

    /**
     * Gets the top edge of the screen in Model View coordinates
     *
     * @return top edge of the screen in Model View coordinates
     */
    public float screenEdgeTop(){
        return (-1)*(eyeDistance / frustumStart);
    }

    /**
     * Gets the bottom edge of the screen in Model View coordinates
     *
     * @return bottom edge of the screen in Model View coordinates
     */
    public float screenEdgeBottom(){
        return (1)*(eyeDistance / frustumStart);
    }

    /**
     * Returns the current GL object
     *
     * @return A reference to the current GL10 object
     */
    public GL10 getGLObj(){
        return glRef;
    }

    /**
     * Allows us to store a reference to the Surface view running this renderer
     *
     * @param newView reference to the surface view
     */
    public void setView(tbdSurfaceView newView){
        view = newView;
    }

    /**
     * Loads a texture given a resource ID
     *
     * @param resource Int resource of texture
     * @return int Texture id as it is known by OpenGl
     */
    public int loadTexture(int resource){
        if(!resourceToTextureIdMap.containsKey(resource)){
            int textureId = textureCount;
            resourceToTextureIdMap.put(resource, textureId);

            Bitmap bitmap = BitmapFactory.decodeResource(view.getContext().getResources(), resource);

            glRef.glBindTexture(GL10.GL_TEXTURE_2D, textures[textureId]);

            // Create Nearest Filtered Texture
            glRef.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_LINEAR);
            glRef.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            glRef.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_REPEAT);
            glRef.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_REPEAT);

            // Use the Android GLUtils to specify a two-dimensional texture image
            // from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            textureCount++;

            return textureId;
        } else {
            return resourceToTextureIdMap.get(resource);
        }
    }

    /**
     * Retrieves given texture given its ID
     * @param textureId int
     * @return int texture as OpenGL knows it
     */
    public int getTexture(int textureId){
        return textures[textureId];
    }

    /**
     * Calls initial gl commands when mRenderer is created
     *
     * @param gl reference to our object for OpenGl
     * @param eglConfig
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        glRef = gl;

        glRef.glGenTextures(MAX_TEXTURES-1, textures, 0);

        // Set the background frame color
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        // Enable use of vertex arrays
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //Enable Transparency
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        world = new World(this, levelFile);
        view.setPlayer(world.getPlayer());
    }

    /**
     * Called on every frame draw
     *
     * @param gl reference to our object for OpenGl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        if(world.isLoaded()){
            Player player = world.getPlayer();

            // Redraw background color
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            // Set GL_MODELVIEW transformation mode
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();   // reset the matrix to its default state

            // When using GL_MODELVIEW, you must set the view point
            GLU.gluLookAt(gl, -player.getPositionX(), 0f, -1*eyeDistance, -player.getPositionX(), 0f, 0f, 0f, 1.0f, 0.0f);

            gl.glTranslatef(0f, -eyeDistance/frustumStart, 0f);

            for(Drawable drawable : world.getWorldDrawables()){
                gl.glPushMatrix();
                drawable.draw(gl);
                gl.glPopMatrix();
            }

            for(Drawable drawable : world.getEnemies()){
                gl.glPushMatrix();
                drawable.draw(gl);
                gl.glPopMatrix();
                drawable.update();
            }

            gl.glPushMatrix();
            player.draw(gl);
            gl.glPopMatrix();
            player.update();
        }

        world.checkCollisions();
    }

    /**
     * Updates the perspective projection matrix when the surface dimensions change
     *
     * @param gl reference to our object for OpenGl
     * @param newWidth New width of the screen
     * @param newHeight New height of the screen
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int newWidth, int newHeight) {
        glRef = gl;
        height = newHeight;
        width = newWidth;

        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, frustumStart, frustumEnd);  // apply the projection matrix
    }
}
