#version 120

out vec3 normal;
out vec2 texcoord;

out vec4 FragColor;

void main() {

    FragColor = vec4(normal, 1.0);
}

