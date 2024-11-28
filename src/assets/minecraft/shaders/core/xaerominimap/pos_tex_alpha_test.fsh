#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float DiscardAlpha;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    fragColor = texture(Sampler0, texCoord0) * ColorModulator;
    if (fragColor.a <= DiscardAlpha) {
        discard;
    }
}
