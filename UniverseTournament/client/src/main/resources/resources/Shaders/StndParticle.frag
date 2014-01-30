#version 130

in float alpha;
in vec3 lightdir;

uniform sampler2D diffuse_sampler;

out vec4 FragColor;

void main() {
    vec4 color = texture2D(diffuse_sampler, gl_PointCoord, 0.);
    color.a *= alpha;

    FragColor = color;
}

