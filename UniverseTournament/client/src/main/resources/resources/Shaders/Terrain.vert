#version 120
//#include NoiseLib.vert

#if __VERSION__<=120
#define tex(s,t) texture2D(s, t)
#else
#define tex(s,t) texture(s, t)
#endif

const float cell_width = 1./512.;
const float cell_height = 1./256.;
const vec2 textureSize = vec2(512., 256.);
const vec2 texelSize = vec2(1.0f / textureSize.x, 1.0f / textureSize.y);

//#extension GL_EXT_gpu_shader4 : require
#if __VERSION__==120
attribute vec4 in_position;
varying vec4 ambient, diffuse, specular;
varying vec3 normal;
varying vec2 texcoord;
#else
in vec4 in_position;
out vec4 ambient, diffuse, specular;
out vec3 normal;
out vec2 texcoord;
#endif

uniform mat4 mat_m, mat_v, mat_p;
uniform mat3 mat_n;

//TODO: später durch const ersetzten
uniform vec4 light_diffuse;
uniform vec4 light_ambient;
uniform vec4 light_specular;

uniform vec4 mat_diffuse;
uniform vec4 mat_ambient;
uniform vec4 mat_specular;

uniform sampler2D heightmap;

uniform float displacement = 0.3;

//functions
vec4 calcNormal(vec2 position);

void main() {
    texcoord = in_position.xy;

    ambient = mat_ambient * light_ambient + 0.1 * mat_ambient;
    diffuse = mat_diffuse * light_diffuse;
    specular = mat_specular * light_specular;

    vec4 nh = calcNormal(texcoord); //vec3 normal and float heigth
    mat3 nm = mat_n;
    nm[2] += nm[2] * displacement;
    normal =  mat3(mat_v) * nm * nh.xyz;

    vec4 position = in_position;
    position.z += displacement * nh.w;

    gl_Position = mat_p * mat_v * mat_m * position;

}

vec4 calcNormal(vec2 position){
    vec4 normal;
    normal.w = tex(heightmap, position).x;
    float left = tex(heightmap, vec2(position.x - cell_width, position.y)).x;
    float right = tex(heightmap, vec2(position.x + cell_width, position.y)).x;
    float top = tex(heightmap, vec2(position.x , position.y + cell_height)).x;
    float bottom = tex(heightmap, vec2(position.x , position.y - cell_height)).x;
    /*vec3 v1 = normalize(vec3(cell_width, 0., normal.w-left));
    vec3 v2 = normalize(vec3(0., cell_height, normal.w-bottom));
    vec3 c1 = cross(v1, v2);

    vec3 v3 = normalize(vec3(-cell_width, 0., normal.w-right));
    vec3 v4 = normalize(vec3(0., -cell_height, normal.w-top));
    vec3 c2 = cross(v3, v4);

    normal.xyz = normalize(c1+c2);*/

    vec3 v1 = normalize(vec3(2. * cell_width, 0., right - left));
    vec3 v2 = normalize(vec3(0., 2. * cell_height, top - bottom));

    normal.xyz = normalize(cross(v1, v2));
    return normal;
}
