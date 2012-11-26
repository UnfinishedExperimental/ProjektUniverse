#version 130

#if __VERSION__==120
    varying vec4 ambient, diffuse;
    varying vec3 normal;//, tangent, bitangent;texcoor
    varying vec2 texcoord;
    varying float specexp, specular;
#else
    in vec4 ambient, diffuse;
    in vec3 normal;//, tangent, bitangent;texcoor
    in vec2 texcoord;
    in float specexp, specular;

    out vec4 FragColor[3];
#endif

uniform vec4 smapler_settings;
uniform sampler2D diffuse_sampler, specular_sampler, normal_sampler, alpha_sampler;

void main() {
    if(smapler_settings.w != 0.0){
        #if __VERSION__==120
            if(texture2D(alpha_sampler, texcoord).r < 0.5)
                discard;
        #else
            if(texture(alpha_sampler, texcoord).r < 0.5)
                discard;
        #endif
    }

    vec4 color, dcolor;
    color = vec4(0,0,0,0);
    dcolor = diffuse;
    if(smapler_settings.x > 0.5){
        #if __VERSION__==120
            dcolor *= texture2D(diffuse_sampler, texcoord, 0.);
        #else
            dcolor *= texture(diffuse_sampler, texcoord, 0.);
        #endif
    }

    vec3 N, L;
    float NdotL;

    N = normalize(normal);

    float scolor;
    scolor = specular;
    if(smapler_settings.y != 0.0){
        #if __VERSION__==120
            scolor *= texture2D(specular_sampler, texcoord, 0.).r;
        #else
            scolor *= texture(specular_sampler, texcoord, 0.).r;
        #endif
    }

        //vec3 tn = normalize(texture2D(normal_sampler, texcoor, 0.).xyz);
        //if(settings.z != 0.0)
        // N = normalize(tangent*tn.x+bitangent*tn.z+normal*tn.y);

    // write Total Color:
        dcolor.a = scolor;
        N = (N+1)*0.5;
        vec4 norspecex = vec4(N, specexp);
    #if __VERSION__==120
        gl_FragData[0] = dcolor;
        gl_FragData[1] = norspecex;
        gl_FragData[2] = ambient*dcolor;
    #else
        FragColor[0] = dcolor;
        FragColor[1] = norspecex;
        FragColor[2] = ambient*dcolor;
    #endif
}

