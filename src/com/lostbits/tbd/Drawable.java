package com.lostbits.tbd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author Eric Parsons
 * @version Final0
 *
 * Class that provides default functionality for drawable objects
 */
abstract public class Drawable {
    private tbdGLES10Renderer renderer;
    private FloatBuffer vertices = null;
    private int vertexCount = 0;
    private FloatBuffer textureBuffer = null;
    private int textureCoordCount = 0;

    private float width = 0.5f;
    private float height = 0.5f;

    private float angleXY = 0f;
    private float angleXYDeltaTheta = 0.0f;
    private float angleXZ = 0f;
    private float angleXZDeltaTheta = 0.0f;
    private float positionX = 0f;
    private float positionY = 0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float dx = 0f;
    private float dy = 0f;
    private int color = Color.MAGENTA; //Default color

    private boolean hasTexture = false;
    private int textureId;
    private int[] textures = new int[1];

    private boolean initialized = false;

    //Getters and Setters

    public tbdGLES10Renderer getRenderer() {
        return renderer;
    }

    public void setRenderer(tbdGLES10Renderer renderer) {
        this.renderer = renderer;
    }

    public float getAngleXY() {
        return angleXY;
    }

    public void setAngleXY(float angleXY) {
        this.angleXY = angleXY;
    }

    public float getAngleXZ() {
        return angleXZ;
    }

    public void setAngleXZ(float angleXZ) {
        this.angleXZ = angleXZ;
    }

    public float getAngleXZDeltaTheta() {
        return angleXZDeltaTheta;
    }

    public void setAngleXZDeltaTheta(float angleXZDeltaTheta) {
        this.angleXZDeltaTheta = angleXZDeltaTheta;
    }

    public float getAngleXYDeltaTheta() {
        return angleXYDeltaTheta;
    }

    public void setAngleXYDeltaTheta(float angleXYDeltaTheta) {
        this.angleXYDeltaTheta = angleXYDeltaTheta;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLeft(){
        return positionX - width/2;
    }

    public float getRight(){
        return positionX + width/2;
    }

    public float getTop(){
        return positionY + height/2;
    }

    public float getBottom(){
        return positionY - height/2;
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        width = width*scale;
        height = height*scale;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        height = height*scaleY;
        this.scaleY = scaleY;

    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        width = width*scaleX;
        this.scaleX = scaleX;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    /**
     * Takes in an array of vertices and constructs a float buffer
     *
     * @param vertices
     */
    public void setVertices(float[] vertices) {
        // initialize vertex Buffer for triangle  (# of coordinate values * 4 bytes per float)
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);

        // use the device hardware's native byte order
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer newDrawable = vbb.asFloatBuffer();
        newDrawable.put(vertices);

        // set the buffer to read the first coordinate
        newDrawable.position(0);

        this.vertices = newDrawable;
        this.vertexCount = vertices.length/3;
    }

    /**
     * Loads a texture coordinate array that parallels the vertex array.
     *
     * @param textureCoords Float array of texture coords
     */
    public void setTextureCoords(float[] textureCoords) {
        // initialize Texture coord Buffer for triangle  (# of coordinate values * 4 bytes per float)
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length * 4);

        // use the device hardware's native byte order
        tbb.order(ByteOrder.nativeOrder());
        FloatBuffer newTextureMap = tbb.asFloatBuffer();
        newTextureMap.put(textureCoords);

        // set the buffer to read the first coordinate
        newTextureMap.position(0);

        this.textureBuffer = newTextureMap;
        this.textureCoordCount = textureCoords.length/2;
    }

    /**
     * Getter for the texture buffer created by setTextureCoords
     *
     * @return FloatBuffer contains raw texture coords
     */
    public FloatBuffer getTextureBuffer(){
        return textureBuffer;
    }

    /**
     * Applies a texture to the drawable if texture coords were provided
     *
     * @param resource Resource id from R class
     */
    public void setTexture(int resource){
        if(textureBuffer != null){
            textureId = renderer.loadTexture(resource);
            hasTexture = true;
        } else {
            Log.d("Drawable", "Texture could not be loaded since texture buffer not initialized");
        }
    }

    /**
     * Sets color of drawable.  Use Color.argb() to construct parameter
     *
     * @param newColor 32-bit value with 8bits for each color (Use Color class)
     */
    public void setColor(int newColor){
        color = newColor;
    }

    /**
     * Returns a buffer of the drawable vertices
     *
     * @return Floatbuffer of vertices
     */
    public FloatBuffer getVertices() {
        return vertices;
    }

    /**
     * Returns the number of vertices that make up this drawable
     *
     * @return int Count of vertices
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Sets default position
     *
     * @param renderer Reference to mRenderer so that we can get information about the screen (such as bounds)
     */
    public void initDrawable(tbdGLES10Renderer renderer) {
        this.renderer = renderer;
        initialized = true;
    }

    /**
     * Returns true if object is in a state that can be drawn
     *
     * @return boolean true if initialize
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Draw the drawable on the screen
     *
     * @param gl Reference to gl object for drawing with OpenGl.
     */
    public void draw(GL10 gl) {

        gl.glEnable(GL10.GL_ALPHA_BITS);
        if(hasTexture){
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, renderer.getTexture(textureId));
        }else{
            //Set Color
            gl.glColor4f(Color.red(color)/255.0f, Color.green(color)/255.0f, Color.blue(color)/255.0f, Color.alpha(color)/255.0f);
        }

        //Position object on screen
        gl.glTranslatef(-positionX, positionY, 0);

        // Rotate object
        gl.glRotatef(angleXY, 0.0f, 0.0f, 1.0f);
        gl.glScalef(scaleX, scaleY, 1.0f);

        // Draw the object
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);

        if(hasTexture){
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }
        gl.glDisable(GL10.GL_ALPHA_BITS);
    }

    /**
     * Updates the object when called.  Called
     */
    public void update(){
        positionX += dx;
        positionY += dy;
        angleXY += angleXYDeltaTheta;
    }
}
