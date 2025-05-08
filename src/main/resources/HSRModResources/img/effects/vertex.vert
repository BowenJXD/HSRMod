#ifdef GL_ES
precision mediump float;
#endif

attribute vec4 a_position; // Vertex position
attribute vec4 a_color;    // Vertex color
attribute vec2 a_texCoord0; // Texture coordinate

uniform mat4 u_projTrans; // Projection and transformation matrix

varying vec4 v_color;     // Color passed to the fragment shader
varying vec2 v_texCoords; // Texture coordinates passed to the fragment shader

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}
