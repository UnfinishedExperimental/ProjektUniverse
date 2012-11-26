#version 130

uniform sampler2D screen;
uniform float texstep;

out vec4 FragColor;

void main() {
    float c = texture2D(screen, gl_TexCoord[0].st, 0.).r;
    if(c>0.1)
        c=1.;
    else
        c=0.;
    FragColor = vec4(c, c, c, 1.);
}

