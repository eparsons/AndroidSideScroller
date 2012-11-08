package com.lostbits.tbd;

/**
 * @author Eric Parsons
 * @version Final1
 *
 * Date: 4/12/12
 * Time: 6:59 PM
 */
public class Physics {
    /**
     * Emulates acceleration by decrementing vertical velocity
     * @param heavyObject
     */
    static public void applyGravity(Drawable heavyObject){
        float dy = heavyObject.getDy();
        heavyObject.setDy(dy - 0.003f);
    }

}
