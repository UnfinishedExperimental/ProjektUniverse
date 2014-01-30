#version 120

#if __VERSION__<=120
#define tex(s,t) texture2D(s, t, 0.)
#else
#define tex(s,t) texture(s, t, 0.)
#endif

#if __VERSION__==120
varying vec4 ambient, diffuse, specular;
varying vec3 normal;//, lightdir;/, tangent, bitangent;texcoor
varying vec2 texcoord;
varying vec3 viewdir;
#else
in vec4 ambient, diffuse, specular;
in vec3 normal;//, lightdir;//, tangent, bitangent;texcoor
in vec2 texcoord;
in vec3 viewdir;

out vec4 FragColor;
#endif

uniform vec3 halfvector;
uniform vec3 light_pos;

uniform float mat_spec_exp;

uniform vec4 smapler_settings;
uniform sampler2D diffuse_sampler, specular_sampler, normal_sampler, alpha_sampler;
uniform samplerCube env;

void main() {
    if(smapler_settings.w != 0.0){
        if(tex(alpha_sampler, texcoord).r < 0.5)
            discard;
    }

    vec4 color, dcolor;
    color = vec4(0,0,0,0);
    dcolor = diffuse;
    dcolor *= tex(diffuse_sampler, texcoord)*smapler_settings.x;

    vec3 N, L;
    float NdotL;

    N = normalize(normal);
    L = light_pos;
    NdotL = dot(N, L);

    vec4 scolor;
    scolor = specular;
    scolor *= tex(specular_sampler, texcoord) * smapler_settings.y;

    //vec3 tn = normalize(texture2D(normal_sampler, texcoor, 0.).xyz);
    //if(settings.z != 0.0)
    // N = normalize(tangent*tn.x+bitangent*tn.z+normal*tn.y);

        float HdotN = dot(N, halfvector);
        float spec = pow(max(HdotN, 0.), mat_spec_exp);
    if(NdotL >= 0.0){
        color += scolor * spec;
        color += dcolor * NdotL;
    }


    color += ambient;

    //vec3 N = normalize(normal);
    vec3 lookup = normalize(viewdir);

    color = mix(color, textureCube(env, lookup, 0.), (1.-spec)*0.6);
    // write Total Color:
    #if __VERSION__==120
    gl_FragColor = color;
    #else
    FragColor = color;
    #endif
}

