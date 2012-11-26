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
        vec4 color = texture2D(diffuse_sampler, texcoord, 0.);
        if(color.a < 0.5)
            discard;
        gl_FragColor = color;
    #else
        FragColor = texture(diffuse_sampler, texcoord, 0.);
        if(FragColor.a < 0.5)
            discard;
    #endif
}

