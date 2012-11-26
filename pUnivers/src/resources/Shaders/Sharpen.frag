#version 140

uniform sampler2D screen;
uniform float texstep;

in vec2 texcoord;
out vec4 FragColor;

vec2 offset[9] = vec2[](
    vec2( -texstep, -texstep ), vec2( 0, -texstep ), vec2( texstep, -texstep ),
    vec2( -texstep, 0 ), vec2( 0, 0 ), vec2( texstep, 0 ),
    vec2( -texstep, texstep ), vec2( 0, texstep ), vec2( texstep, texstep )
);
float kernel[9] = float[](
    -1.0, -1.0, -1.0,
    -1.0, 8.0, -1.0,
    -1.0, -1.0, -1.0
);

void main(){
    vec4 col;
    for ( int i = 0; i < 9; i++ ){
        col += texture2D( screen, texcoord + offset[i] ) * kernel[i];
    }
    col = texture2D( screen, texcoord) + col * 0.5;
    FragColor = clamp(col, 0., 1.);
}

