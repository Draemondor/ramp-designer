#pragma once
//Std libs
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include <vector>
//Dependencies
#include <glad/glad.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>

using namespace std;

struct Vertex {
	glm::vec3 Position;
	glm::vec3 Normal;
	glm::vec2 TexCoords;
};

struct Texture {
	GLuint id;
	string type;
	aiString path;
};

class Mesh {
public:
	//Mesh input data
	vector<Vertex> vertices;
	vector<GLuint> indices;
	vector<Texture> textures;

	//Mesh Constructor
	Mesh(vector<Vertex> vertices, vector<GLuint> indices, vector<Texture> textures) {
		this->vertices = vertices;
		this->indices = indices;
		this->textures = textures;

		//Set vertex buffers and attribute pointers once required data is complete;
		this->setupMesh();
	}

	//Render mesh
	void Draw(Shader shader) {

		//Bind textures
		GLuint diffuseNr = 1;
		GLuint specularNr = 1;
		
		for (GLuint i = 0; i < this->textures.size(); i++) {
			//Activate texture before binding
			glActiveTexture(GL_TEXTURE0 + i);
			stringstream ss;
			string number;
			string name = this->textures[i].type;
			if (name == "texture_diffuse") {
				//Send GLuint to stream
				ss << diffuseNr++;
			}
			else if (name == "texture_specular") {
				//Send GLuint to stream
				ss  << specularNr++;
			}
			
			number = ss.str();
			//Set sampler to correct texture uint and bind
			glUniform1i(glGetUniformLocation(shader.ID, (name + number).c_str()), i);
			glBindTexture(GL_TEXTURE_2D, this->textures[i].id);
		}

		//Draw Mesh
		glBindVertexArray(this->VAO);
		glDrawElements(GL_TRIANGLES, this->indices.size(), GL_UNSIGNED_INT, 0);
		glBindVertexArray(0);

		//Set back to defaults once configured
		for (GLuint i = 0; i < this->textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}

private:
	GLuint VAO, VBO, EBO;
	//initialize all buffer objects/arrays
	void setupMesh() {
		//Create buffers/arrays
		glGenVertexArrays(1, &this->VAO);
		glGenBuffers(1, &this->VBO);
		glGenBuffers(1, &this->EBO);
		/*
		Load data into buffers 
		{pass a pointer to the struct and translate to a glm::vec3/2 array 
		..which translates to 3/2 floats which translates to a byte array.}
		*/
		glBindVertexArray(this->VAO);
		glBindBuffer(GL_ARRAY_BUFFER, this->VBO);
		glBufferData(GL_ARRAY_BUFFER, this->vertices.size() * sizeof(Vertex), &this->vertices[0], GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this->EBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, this->indices.size() * sizeof(GLuint), &this->indices[0], GL_STATIC_DRAW);

		//Set vertex attribute pointers
		//Vertex Position
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (GLvoid*)0);
		//Vertex Normal
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(Vertex), (GLvoid*)offsetof(Vertex, Normal));
		//Vertex Texture coordinates
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, sizeof(Vertex), (GLvoid*)offsetof(Vertex, TexCoords));
		
		glBindVertexArray(0);
	}

};

