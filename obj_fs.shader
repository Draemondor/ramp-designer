#version 330 core
out vec4 FragColor;

in vec3 Normal;
in vec3 FragPosition;

uniform vec3 lightPosition;
uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 viewPosition;

uniform sampler2D texture_1;

void main()
{
    //Ambient lighting
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;
   
   //Diffuse lighting
    vec3 norm = normalize(Normal);
    vec3 lightDirection = normalize(lightPosition - FragPosition);
    float difference = max(dot(norm, lightDirection), 0.0f);
    vec3 diffuse = difference * lightColor;

    //Specular lighting
    float specularStrength = 0.5f;
    vec3 viewDirection = normalize(viewPosition - FragPosition);
    vec3 reflectDirection = reflect(-lightDirection, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0f), 32);
    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient + diffuse + specular) * objectColor;
    FragColor = vec4(result, 1.0f);
}