package io.yedox.imagine3d.util;

public class AABB {
    public float x_a;
    public float y_a;
    public float z_a;

    public float x_b;
    public float y_b;
    public float z_b;

    public AABB(float x, float y, float z, float a, float b, float c) {
        x_a = x;
        y_a = y;
        z_a = z;
        x_b = a;
        y_b = b;
        z_b = c;
    }

    /**
     * Check if specified AABB intersects
     * with the given AABB
     * @param aabb The AABB to check
     */
    public boolean intersectsAABB(AABB aabb) {
        if (aabb.x_b <= x_a || aabb.x_a >= x_b) {
            return false;
        }
        if (aabb.y_b <= y_a || aabb.y_a >= y_b) {
            return false;
        }
        return !(aabb.z_b <= z_a) && !(aabb.z_a >= z_b);
    }
}
