#version 120

#if __VERSION__==120
    attribute vec4 in_position;
    attribute float in_size;
    attribute float in_alpha;
    varying float alpha;
    varying vec3 lightdir;
#else
    in vec4 in_position;
    in float in_size;
    in float in_alpha;
    out float alpha;
    out vec3 lightdir;
#endif

uniform mat4 mat_m, mat_v, mat_p;
uniform vec4 light_pos;

void main() {
    gl_Position =  mat_m * in_position;
    vec4 lpos = mat_v * light_pos - gl_Position;
    lightdir = vec4(light_pos - gl_Position).xyz;

    gl_Position = mat_v * gl_Position;
    gl_PointSize = 10 * in_size / - gl_Position.z;
    alpha = in_alpha;

    gl_Position = mat_p * gl_Position;
}
