package com.android.texample2;

// NOTE: This was copied from https://github.com/d3alek/Texample2/blob/master/Texample2/src/com/android/texample2/TextureRegion.java
// Revision 8b69e4f6cad45a6de14b9c99d2e4a705457cbcad
// Licensed under CC0 1.0 Public Domain.

class TextureRegion {

    //--Members--//
    public float u1, v1;                               // Top/Left U,V Coordinates
    public float u2, v2;                               // Bottom/Right U,V Coordinates

    //--Constructor--//
    // D: calculate U,V coordinates from specified texture coordinates
    // A: texWidth, texHeight - the width and height of the texture the region is for
    //    x, y - the top/left (x,y) of the region on the texture (in pixels)
    //    width, height - the width and height of the region on the texture (in pixels)
    public TextureRegion(float texWidth, float texHeight, float x, float y, float width, float height)  {
        this.u1 = x / texWidth;                         // Calculate U1
        this.v1 = y / texHeight;                        // Calculate V1
        this.u2 = this.u1 + ( width / texWidth );       // Calculate U2
        this.v2 = this.v1 + ( height / texHeight );     // Calculate V2
    }
}