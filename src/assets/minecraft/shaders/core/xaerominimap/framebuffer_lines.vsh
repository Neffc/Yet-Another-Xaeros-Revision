#version 150

in vec3 Position;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float LineWidth;
uniform vec2 FrameSize;

out vec4 vertexColor;

void main() {
	vec4 startPos = ProjMat * ModelViewMat * vec4(Position, 1.0);
	vec4 endPos = ProjMat * ModelViewMat * vec4(Position + Normal, 1.0);
	vec3 startPosNormalized = startPos.xyz / startPos.w;
	vec3 endPosNormalized = endPos.xyz / endPos.w;
	vec2 lineVectorDoubledFrameSpace = FrameSize * (endPosNormalized.xy - startPosNormalized.xy);//result is doubled because clip space coordinates are in range (-1.0;1.0)
	vec2 lineDirectionFrameSpace = normalize(lineVectorDoubledFrameSpace);
	vec3 lineWidthOffsetClipSpace = vec3(LineWidth * vec2(-lineDirectionFrameSpace.y, lineDirectionFrameSpace.x) / FrameSize, 0.0);//double by LineWidth and then halved by /FrameSize
	if(lineWidthOffsetClipSpace.x < 0.0){
		lineWidthOffsetClipSpace *= -1.0;
	}
	vec3 offsetPositionClipSpace;
	if(gl_VertexID % 2 == 0){
		offsetPositionClipSpace = startPosNormalized + lineWidthOffsetClipSpace;
	} else {
		offsetPositionClipSpace = startPosNormalized - lineWidthOffsetClipSpace;
	}
	gl_Position = vec4(offsetPositionClipSpace, 1);//already normalized, so w is 1
    vertexColor = Color;
}
