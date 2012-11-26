#version 120
#include NoiseLib.vert

//#extension GL_EXT_gpu_shader4 : require
#if __VERSION__==120
    attribute vec4 in_position;
    attribute vec2 in_texcoord;
    attribute vec3 in_normal;
    varying vec4 ambient, diffuse, specular;
    varying vec3 normal;//, lightdir;//, tangent, bitangent;
    varying vec2 texcoord;
    varying vec3 viewdir;
#else
    in vec4 in_position;
    in vec2 in_texcoord;
    in vec3 in_normal;
    out vec4 ambient, diffuse, specular;
    out vec3 normal;//, lightdir;//;//, tangent, bitangent;
    out vec2 texcoord;
    out vec3 viewdir;
#endif

//uniform samplerBuffer instance_sampler;

uniform mat4 mat_m, mat_v, mat_p;
uniform mat3 mat_n;

uniform vec4 light_diffuse;
uniform vec4 light_ambient;
uniform vec4 light_specular;

uniform vec4 mat_diffuse;
uniform vec4 mat_ambient;
uniform vec4 mat_specular;


void main() {
    //vec4 transform = texelFetch(instance_sampler, 1);//gl_InstanceID);
    //vec4 vertex = vec4((in_position.xyz + transform.xyz),in_position.w);

    texcoord = in_texcoord;

    ambient = mat_ambient * light_ambient + vec4(0.1, 0.1, 0.1, 0.1)*mat_ambient;
    diffuse = mat_diffuse * light_diffuse;
    specular = mat_specular * light_specular;

    /*float i = noise(in_position.xyz);
    diffuse = ((i+1.)*0.5) * light_diffuse;
    diffuse *= diffuse;
    diffuse *= light_diffuse;
    // displacement along normal*/
    normal = mat_n * in_normal;

    //vec4 position = in_position + (vec4(normal, 1.0) * i * displacement);
    //position.w = 1.0;



	// find world space position.
    gl_Position = mat_m * in_position;

	// find world space normal.
	normal = normalize( normal );

	// find world space eye vector.
	vec3 E = normalize( gl_Position.xyz - vec4(mat_v * vec4(0)).xyz);

	// calculate the reflection vector in world space.
	viewdir = -reflect( E, normal);

        normal = mat3(mat_v) * normal;

    gl_Position = mat_p * mat_v * gl_Position;

}
