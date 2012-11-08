package com.lostbits.tbd;

/**
 * @author Eric Parsons
 * @version Final1
 *
 * Base class for player and ai characters
 */

public abstract class GameEntity extends Drawable{
    private final float COLLISION_EXCLUSION = 1/7.5f;


    private boolean inPlay = true;

    private World level;
    private boolean onGround = false;

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    /**
     * Take action if we've collided on our left
     *
     * @param struckObject Drawable that we struck
     */
    abstract public void handleLeftCollision(Drawable struckObject);

    /**
     * Take action if we've collided on our right
     *
     * @param struckObject Drawable that we struck
     */
    abstract public void handleRightCollision(Drawable struckObject);

    /**
     * Take action if we've collided on our top
     *
     * @param struckObject Drawable that we struck
     */
    abstract public void handleTopCollision(Drawable struckObject);

    /**
     * Take action if we've collided on our bottom
     *
     * @param struckObject Drawable that we struck
     */
    abstract public void handleBottomCollision(Drawable struckObject);

    /**
     * Determine is entity is "in the air"
     *
     * @return returns true is entity is not off of ground surface of world.
     */
    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    /**
     * Game Entities are affected by gravity
     */
    public void update(){
        super.update();
        Physics.applyGravity(this);
    }

    /**
     * Checks for collsions with the given drawable
     *
     * @param otherDrawable Drawable to check for collisions
     * @return boolean true if collision occurred
     */
    public boolean collision(Drawable otherDrawable){
        if(getBottom() > otherDrawable.getTop())
            return false;
        if(getLeft() > otherDrawable.getRight())
            return false;
        if(getRight() < otherDrawable.getLeft())
            return false;
        if(getTop() < otherDrawable.getBottom())
            return false;

        return true;
    }

    /**
     * Checks for collsions with the given drawable on our left
     *
     * @param otherDrawable Drawable to check for collisions
     * @return boolean true if collision occurred on left
     */
    public boolean collisionLeft(Drawable otherDrawable){
        if(getBottom()+getHeight()*COLLISION_EXCLUSION > otherDrawable.getTop())
            return false;
        if(getTop()-getHeight()*COLLISION_EXCLUSION < otherDrawable.getBottom())
            return false;
        return (getLeft() <= otherDrawable.getRight() &&
                getPositionX() >= otherDrawable.getRight());
    }

    /**
     * Checks for collsions with the given drawable on our right
     *
     * @param otherDrawable Drawable to check for collisions
     * @return boolean true if collision occurred on right
     */
    public boolean collisionRight(Drawable otherDrawable){
        if(getBottom()+getHeight()*COLLISION_EXCLUSION > otherDrawable.getTop())
            return false;
        if(getTop()-getHeight()*COLLISION_EXCLUSION < otherDrawable.getBottom())
            return false;
        return (getRight() >= otherDrawable.getLeft() &&
                getPositionX()-getWidth()*COLLISION_EXCLUSION <= otherDrawable.getLeft());
    }

    /**
     * Checks for collsions with the given drawable on our bottom
     *
     * @param otherDrawable Drawable to check for collisions
     * @return boolean true if collision occurred on bottom
     */
    public boolean collisionBottom(Drawable otherDrawable){
        if(getLeft()+getWidth()*COLLISION_EXCLUSION > otherDrawable.getRight())
            return false;
        if(getRight()-getWidth()*COLLISION_EXCLUSION < otherDrawable.getLeft())
            return false;
        return (getBottom() <= otherDrawable.getTop() &&
                getPositionY() >= otherDrawable.getTop());
    }

    /**
     * Checks for collsions with the given drawable on our top
     *
     * @param otherDrawable Drawable to check for collisions
     * @return boolean true if collision occurred on top
     */
    public boolean collisionTop(Drawable otherDrawable){
        if(getLeft()+getWidth()*COLLISION_EXCLUSION > otherDrawable.getRight())
            return false;
        if(getRight()-getWidth()*COLLISION_EXCLUSION < otherDrawable.getLeft())
            return false;
        return (getTop() >= otherDrawable.getBottom() &&
                getPositionY() <= otherDrawable.getBottom());
    }
}
