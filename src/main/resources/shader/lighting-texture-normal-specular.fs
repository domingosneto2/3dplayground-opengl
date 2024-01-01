#version 330 core

// Fragment shader with Texture and Normal Map support.

in vec3 FragPos;
in vec3 MyNormal;
in vec2 TexCoord;
in mat3 TBN;

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;

uniform vec4 color;
uniform vec3 lightPos;
uniform vec4 lightColor;
uniform vec4 ambientColor;
uniform vec3 cameraPos;
uniform float specularStrength;
uniform float specularFactor;

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    float diffusePower;
    float specularPower;
    float ambientPower;

    float cutoff;
    float cutoffSmoothing;

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};
#define NR_POINT_LIGHTS 4
uniform PointLight pointLights[NR_POINT_LIGHTS];

uniform int numPointLights;

struct DirectionalLight {
    vec3 direction;

    float diffusePower;
    float specularPower;
    float ambientPower;

    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};
#define NR_DIRECTIONAL_LIGHTS 4
uniform DirectionalLight directionalLights[NR_DIRECTIONAL_LIGHTS];

uniform int numDirectionalLights;

out vec4 FragColor;

vec4 CalcDirectionalLight(DirectionalLight light, vec3 FragPos)
{
    vec4 texColor = texture(texture1, TexCoord);
    vec3 texNormal = texture(texture2, TexCoord).rgb;
    vec4 texSpecular = texture(texture3, TexCoord);
    texNormal = texNormal * 2.0 - 1.0;
    texNormal = normalize(TBN * texNormal);

    vec3 normal = texNormal;

    vec3 lightDir = normalize(light.direction);
    float diff = max(dot(normal, lightDir), 0.0);

    vec3 reflectDir = reflect(-lightDir, normal);
    vec3 eyeDir = normalize(cameraPos - FragPos);
    float spec = pow(max(dot(eyeDir, reflectDir), 0.0), specularFactor);

    vec4 diffuse  = diff * light.diffuse * light.diffusePower;
    vec4 specular = spec * light.specular * light.specularPower * specularStrength * texSpecular;
    vec4 ambient  = light.ambient * light.ambientPower;

    vec4 result = ((ambient + diffuse) * color * texColor + specular);
    return result;
}


vec4 CalcPointLight(PointLight light, vec3 normal, vec3 FragPos, vec3 viewDir)
{
    vec4 texColor = texture(texture1, TexCoord);
    vec3 texNormal = texture(texture2, TexCoord).rgb;
    vec4 texSpecular = texture(texture3, TexCoord);
    texNormal = texNormal * 2.0 - 1.0;
    texNormal = normalize(TBN * texNormal);

    normal = texNormal;

    vec3 lightDir = normalize(light.position - FragPos);
    float lightDist = length(light.position - FragPos);
    float diff = max(dot(normal, lightDir), 0.0);

    float cutOffSmoothingStart = light.cutoff - light.cutoffSmoothing;
    float cutOffFactor = 1 - clamp((lightDist - cutOffSmoothingStart) / light.cutoffSmoothing, 0, 1);

    vec3 reflectDir = reflect(-lightDir, normal);
    vec3 eyeDir = normalize(cameraPos - FragPos);
    float spec = pow(max(dot(eyeDir, reflectDir), 0.0), specularFactor);

    vec4 diffuse  = diff * light.diffuse * light.diffusePower;
    vec4 specular = spec * light.specular * light.specularPower * specularStrength * vec4(texSpecular);
    vec4 ambient  = light.ambient * light.ambientPower;

    float attenuation = cutOffFactor * 1.0 / (light.constant + light.linear * lightDist + light.quadratic * lightDist * lightDist);

    vec4 result = ((ambient + diffuse) * color * texColor + specular) * attenuation;
    return result;
}

void main()
{
    vec3 normal = normalize(MyNormal);;

    vec4 result = vec4(0, 0, 0, 0);
    for (int i = 0; i < numPointLights; i++)
    {
        result += CalcPointLight(pointLights[i], normal, FragPos, cameraPos);
    }

        for (int i = 0; i < numDirectionalLights; i++)
            {
                result += CalcDirectionalLight(directionalLights[i], FragPos);
            }

    FragColor = vec4(result.xyz, 1.0);
}