#version 120

#if __VERSION__==120
    varying vec2 texcoord;
#else
    in vec2 texcoord;
    out vec4 FragColor;
#endif

uniform sampler2D diffuse_sampler;

void main() {
    #if __VERSION__==120
        gl_FragColor = texture2D(diffuse_sampler, texcoord, 0.);
    #else
        FragColor = texture(diffuse_sampler, texcoord, 0.);
    #endif
}

