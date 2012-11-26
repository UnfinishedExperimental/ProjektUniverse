#version 120

#if __VERSION__==120
    attribute vec2 in_position;
    attribute vec2 in_texcoord;
    varying vec2 texcoord;
#else
    in vec2 in_position;
    in vec2 in_texcoord;
    out vec2 texcoord;
#endif

void main() { 
    // Vertex transformation 
    gl_Position = vec4((in_position-0.5)*2, -1., 1.);
    //gl_Position = sign( gl_Position );
    texcoord = in_texcoord;
}
