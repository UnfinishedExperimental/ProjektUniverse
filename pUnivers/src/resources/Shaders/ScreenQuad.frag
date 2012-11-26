#version 130

#if __VERSION__==120
    varying vec2 texcoord;
    varying vec3 normal;
#else
    in vec2 texcoord;
    in vec3 normal;
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

