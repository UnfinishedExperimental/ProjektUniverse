#version 120

#if __VERSION__==120
#define tex(s,t) texture2D(s, t, 0.)
#else
#define tex(s,t) texture(s, t, 0.)
#endif

#if __VERSION__==120
varying vec4 ambient, diffuse, specular;
varying vec3 normal;
varying vec2 texcoord;
#else
in vec4 ambient, diffuse, specular;
in vec3 normal;
in vec2 texcoord;

out vec4 FragColor;
#endif

uniform vec3 halfvector;
uniform vec3 light_pos;

uniform float mat_spec_exp;

uniform sampler2D diffuse_sampler;

void main() {

    vec4 color, dcolor;
    color = vec4(0,0,0,0);
    dcolor = diffuse;
    dcolor *= tex(diffuse_sampler, texcoord);

    vec3 N;
    float NdotL;

    N = normalize(normal);
    NdotL = dot(N, light_pos);

    if(NdotL >= 0.0){
        float HdotN = dot(N, halfvector);
        color += specular * pow(max(HdotN, 0.), mat_spec_exp);
        color +=  NdotL;
    }

    color += ambient;

    #if __VERSION__==120
    gl_FragColor = color;
    #else
    FragColor = color;//
    #endif
}

