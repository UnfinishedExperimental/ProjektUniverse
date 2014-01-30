
varying vec2  tc;
uniform sampler2D screen;
uniform float texstep;


const mat3 sobelX = mat3(3.0, 0.0, -3.0,
                         10.0, 0.0, -10.0,
                         3.0, 0.0, -3.0);

const mat3 sobelY = mat3(3.0, 10.0, 3.0,
                         0.0, 0.0, 0.0,
                         -3.0, -10.0, -3.0);

float filter(mat3 fil){
    float outp = 0.;
    float minv = 0.;
    float maxv = 0.;

    for(int x =0;x<3;++x){
        for(int y =0;y<3;++y){
            vec2 shiftedtc = tc+vec2(texstep*float(x-1), texstep*float(y-1));
            if(shiftedtc.x<0.0 || shiftedtc.y<0.0 || shiftedtc.x>1.0 || shiftedtc.y>1.0)
                continue;
            if(fil[x][y] < 0.)
                minv += fil[x][y];
            else
                maxv += fil[x][y];

            outp += texture2D(screen, shiftedtc, 0.0).r * fil[x][y];
        }
    }

    return outp/(maxv-minv);
}

void main() {
    // Set the fragment color for example to gray, alpha 1.0
    float sx = filter(sobelX);
    float sy = filter(sobelY);

    float c = abs(sx+sy);
    c = step(0.05, c);
    gl_FragColor = vec4(c, c, c, 1.);
}

