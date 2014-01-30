#version 120

#if __VERSION__==120
    varying vec3 texcoord;
#else
    in vec3 texcoord;
    out vec4 Fragcolor;
#endif

uniform samplerCube diffuse_sampler;

void main() {
    #if __VERSION__==120
        gl_FragColor = textureCube(diffuse_sampler, texcoord, 0.);
    #else
        Fragcolor = textureCube(diffuse_sampler, texcoord, 0.);
    #endif
}

