#version 120

#if __VERSION__==120
    attribute vec4 in_position;
    attribute vec2 in_texcoord;
    attribute vec3 in_normal;
    varying vec2 texcoord;
#else
    in vec4 in_position;
    in vec2 in_texcoord;
    in vec3 in_normal;
    out vec2 texcoord;
#endif

uniform mat4 mat_m, mat_v, mat_p;
uniform mat3 mat_n;


void main() { 
    gl_Position = mat_p * mat_v * mat_m * in_position;//vertex;
    texcoord = in_texcoord;
}
