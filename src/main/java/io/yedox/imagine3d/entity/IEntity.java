package io.yedox.imagine3d.entity;

import processing.core.PApplet;

public interface IEntity {
    /*
     * Called when entity is initialized
     */
    void onEntityInit(Entity entity, PApplet applet);

    /*
     * Called when entity is hurt
     */
    void onEntityHurt(Entity sourceEntity, Entity targetEntity);

    /*
     * Called when entity is destroyed (eg. when entity despawns ir dies)
     */
    void onEntityDestroyed(Entity entity);

    /*
     * Called when entity is drawn
     */
    void draw(Entity entity, PApplet applet);

    /*
    * Called when the user presses a key
    */
    void onKeyPressed(PApplet applet);
}
