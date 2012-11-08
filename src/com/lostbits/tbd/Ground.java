package com.lostbits.tbd;

/**
 * Created by IntelliJ IDEA.
 * User: Eric Parsons
 * Date: 4/3/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @author Eric Parsons
 * @version Final0
 *
 * Ground class of drawables
 */

public class Ground extends Drawable {

    /**
     * Describes a rectangle
     * @param renderer Reference to the OpenGL mRenderer the system is using
     */
    @Override
    public void initDrawable(tbdGLES10Renderer renderer){
        super.initDrawable(renderer);

        float rectangleCoords[] = {
            // X, Y, Z
            -1*(getWidth()/2.0f), -1*(getHeight()/2.0f), 0,
            (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
            -1*(getWidth()/2.0f), (getHeight()/2.0f),    0,
            (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
            (getWidth()/2.0f),    (getHeight()/2.0f),    0,
            -1*(getWidth()/2.0f), (getHeight()/2.0f),    0
        };

        setVertices(rectangleCoords);

        float[] textureCoords = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f
        };
        setTextureCoords(textureCoords);
    }

    /**
     * Ground does not move so we do nothing if update is called
     */
    @Override
    public void update() {

    }
}
