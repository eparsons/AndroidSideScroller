package com.lostbits.tbd;

/**
 * @author Eric Parsons
 * @version Final1
 *
 * Date: 4/12/12
 * Time: 6:19 PM
 */
public class Player extends GameEntity {
    final public float RUN_SPEED = 0.03f;


    /**
     * Initializes the vertices for rendering
     *
     * @param renderer Reference to mRenderer so that we can get information about the screen (such as bounds)
     */
    public void initDrawable(tbdGLES10Renderer renderer){
        super.initDrawable(renderer);

        float[] playerVertices = {
                // X, Y, Z
                -1*(getWidth()/2.0f), -1*(getHeight()/2.0f), 0,
                (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
                -1*(getWidth()/2.0f), (getHeight()/2.0f),    0,
                (getWidth()/2.0f),    -1*(getHeight()/2.0f), 0,
                (getWidth()/2.0f),    (getHeight()/2.0f),    0,
                -1*(getWidth()/2.0f), (getHeight()/2.0f),    0
        };
        setVertices(playerVertices);

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
     * If we fell off the map, reset the player at 1 unit above bottom screen edge
     */
    @Override
    public void update(){
        super.update();
        if(getPositionY() < -2.f){
            setAngleXYDeltaTheta(0.0f);
            setAngleXY(0.0f);
            setPositionY(1.0f);
            setDy(0.0f);
            setDx(0.0f);
            setInPlay(true);
        }
    }

    /**
     * Give Player an upward velocity
     */
    public void jump(){
        if(isOnGround()){
            setDy(0.08f);
            setOnGround(false);
        }
    }

    /**
     * Remove control from the player and have them fall off the world.
     */
    public void killPlayer(){
        setInPlay(false);
        setDx(0.0f);
        setDy(0.04f);
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
        } else if (struckObject instanceof Pacer){
            Pacer pacer = (Pacer)struckObject;
            if(pacer.isInPlay()){
                killPlayer();
                setAngleXYDeltaTheta(5f);
                setDx(.015f);
            }
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
        } else if (struckObject instanceof Pacer){
            Pacer pacer = (Pacer)struckObject;
            if(pacer.isInPlay()){
                killPlayer();
                setAngleXYDeltaTheta(-5f);
                setDx(-.015f);
            }
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
        } else if (struckObject instanceof Pacer){
            Pacer pacer = (Pacer)struckObject;
                if(pacer.isInPlay()){
                killPlayer();
            }
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
        }else if (struckObject instanceof Pacer){
            Pacer enemy = (Pacer)struckObject;
            if(getDy() < 0.0f && isInPlay()){
                enemy.setInPlay(false);
                enemy.setDy(.03f);
                enemy.setScaleY(0.3f);
                setDy(-.7f * getDy());
            }
        }
    }
}
