package com.lostbits.tbd;

/**
 * @author Eric Parsons
 * @version Final1
 *
 * Date: 4/13/12
 * Time: 1:36 PM
 */
public class Pacer extends GameEntity {
    /**
     * Initializes the vertices for rendering
     *
     * @param renderer Reference to mRenderer so that we can get information about the screen (such as bounds)
     */
    public void initDrawable(tbdGLES10Renderer renderer){
        super.initDrawable(renderer);

        float[] pacerVertices = {
                // X, Y, Z
                -1*(getWidth()/2.0f), -1*(getHeight()/2.0f), 0,
                (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
                -1*(getWidth()/2.0f), (getHeight()/2.0f),    0,
                (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
                (getWidth()/2.0f),    (getHeight()/2.0f),    0,
                -1*(getWidth()/2.0f), (getHeight()/2.0f),    0
        };
        super.setVertices(pacerVertices);

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
     * Take action if we've collided on our left
     *
     * @param struckObject Drawable that we struck
     */
    @Override
    public void handleLeftCollision(Drawable struckObject) {
        if(struckObject instanceof Ground){
            Ground ground = (Ground)struckObject;
            setPositionX(ground.getRight() + getWidth()/2);
            setDx(-getDx());
        }
    }

    /**
     * Take action if we've collided on our right
     *
     * @param struckObject Drawable that we struck
     */
    @Override
    public void handleRightCollision(Drawable struckObject) {
        if(struckObject instanceof Ground){
            Ground ground = (Ground)struckObject;
            setPositionX(ground.getLeft() - getWidth()/2);
            setDx(-getDx());
        }
    }

    /**
     * Take action if we've collided on our top
     *
     * @param struckObject Drawable that we struck
     */
    @Override
    public void handleTopCollision(Drawable struckObject) {
        if(struckObject instanceof Ground){
            Ground ground = (Ground)struckObject;
            setPositionY(ground.getBottom() - getHeight()/2);
            setDy(-0.0001f);
        }
    }

    /**
     * Take action if we've collided on our bottom
     *
     * @param struckObject Drawable that we struck
     */
    @Override
    public void handleBottomCollision(Drawable struckObject) {
        if(struckObject instanceof Ground){
            Ground ground = (Ground)struckObject;
            setPositionY(ground.getTop() + getHeight()/2);
            setDy(0.0f);
            setOnGround(true);
        }
    }
}
