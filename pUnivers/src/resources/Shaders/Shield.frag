#version 120

#if __VERSION__==120
    varying vec2 texcoord;
#else
    in vec2 texcoord;
    out vec4 Fragcolor;
#endif

uniform float offset;
uniform vec3 color;

const float pi2 = 3.14159265 * 2;
void main() {
    #if __VERSION__==120
        gl_FragColor = vec4(color, sin(texcoord.y*pi2+sin(texcoord.x*pi2*2+offset*5)+offset*3)*0.3);
    #else
        Fragcolor =vec4(color, sin(texcoord.y*pi2+sin(texcoord.x*pi2*2+offset*5)+offset*3)*0.3);
    #endif
}

