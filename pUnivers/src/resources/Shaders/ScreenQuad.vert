#version 130

#if __VERSION__==120
    attribute vec4 in_position;
    varying vec2 texcoord;
    varying vec3 normal;
#else
    in vec4 in_position;
    out vec3 normal;
    out vec2 texcoord;
#endif

void main(void)
{
    gl_Position = vec4( in_position.xy, 0.0, 1.0 );
    gl_Position = sign( gl_Position );

    texcoord = (gl_Position.xy + 1.0) * 0.5;
    normal = vec3(in_position.xy, 1.0);
}