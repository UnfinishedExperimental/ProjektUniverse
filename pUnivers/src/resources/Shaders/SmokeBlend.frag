#version 120

in vec2 texcoord;
out vec4 FragColor;

uniform sampler2D tex;

void main() {
    vec4 c = texture2D(tex, texcoord, 0.);
    c.r *= c.a;
    c.g *= c.a;
    c.b *= c.a;
    FragColor = c;
}