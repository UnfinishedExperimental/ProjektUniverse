#version 120

#if __VERSION__==120
    attribute vec4 in_position;
    attribute vec2 in_texcoord;
    varying vec2 texcoord;
#else
    in vec4 in_position;
    in vec2 in_texcoord;
    out vec2 texcoord;
#endif

uniform mat4 mat_m, mat_v, mat_p;

void main() {
    // Vertex transformation
    gl_Position = mat_p * mat_v * mat_m * in_position;
    texcoord = in_texcoord*100. +0.5;
}
