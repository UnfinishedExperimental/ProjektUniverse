#version 140

in vec4 in_position;

out vec3 normal;
out vec2 texcoord;

void main(void)
{
    gl_Position = vec4(sign(in_position.xy), 0., 1.);

    texcoord = in_position.xy * 0.5 + 0.5;
    normal = vec3(in_position.xy, 1.0);
}