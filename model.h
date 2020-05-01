#pragma once
//Std libs
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>
#include <map>
#include <vector>
//Dependencies
#include <glad/glad.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <SOIL2.h>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>
//External libs
#include "mesh.h"
#include "shader.h"

using namespace std;

GLint TextureFromFile(const char *path, string directory);

class Model {
public:
	Model(const char* path) {
		this->loadModel(path);
	}

	void Draw(Shader shader) {
		for (GLuint i = 0; i < this->meshes.size(); i++) {
			this->meshes[i].Draw(shader);
		}
	}
	
private:
	//Model attributes
	vector<Mesh> meshes;
	string directory;
	vector<Texture> textures_loaded;

	//Load model w/ assimp extensions
	void loadModel(string path) {
		//Declare assimp importer
		Assimp::Importer importer;
		//Assimp reads file
		const aiScene* scene = importer.ReadFile(path, aiProcess_Triangulate | aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_CalcTangentSpace);
		//Error check
		if (!scene || scene->mFlags & AI_SCENE_FLAGS_INCOMPLETE || !scene->mRootNode) {
			cout << "ERROR::ASSIMP:: " << importer.GetErrorString() << endl;
			return;
		}
		//Model load successful
		//Get directory path
		this->directory = path.substr(0, path.find_last_of('/'));
		
		//Asssimp root node
		//Recurssively process assimp's root nodes
		this->processNode(scene->mRootNode, scene);
	}
	 //Process each mesh
	//Takes in node and scene which are assimp objects
	void processNode(aiNode *node, const aiScene *scene) {
		for (GLuint i = 0; i < node->mNumMeshes; i++) {
			//Node contains indices to index object to scene
			//Scene contains all data  of nodes
			aiMesh* mesh = scene->mMeshes[node->mMeshes[i]];
			this->meshes.push_back(processMesh(mesh, scene));
		}
		//Recurssively Process child nodes
		for (GLuint i = 0; i < node->mNumChildren; i++) {
			this->processNode(node->mChildren[i], scene);
		}
	}

	Mesh processMesh(aiMesh* mesh, const aiScene* scene) {
		//Model attributes
		vector<Vertex> vertices;
		vector<GLuint> indices;
		vector<Texture> textures;

		for (GLuint i = 0; i < mesh->mNumVertices; i++) {
			Vertex vertex;
			//Assimp will use its own vector class later 
			glm::vec3 vector;
			//Positions
			vector.x = mesh->mVertices[i].x;
			vector.y = mesh->mVertices[i].y;
			vector.z = mesh->mVertices[i].z;
			vertex.Position = vector;
			//Normals
			vector.x = mesh->mNormals[i].x;
			vector.y = mesh->mNormals[i].y;
			vector.z = mesh->mNormals[i].z;
			vertex.Normal = vector;
			//Texture coord
			if (mesh->mTextureCoords[0]) {
				glm::vec2 vec;
				vec.x = mesh->mTextureCoords[0][i].x;
				vec.y = mesh->mTextureCoords[0][i].y;
				vertex.TexCoords = vec;
			}
			else {
				//Assign default vaulues of 0.0 if tex coords are not present
				vertex.TexCoords = glm::vec2(0.0f, 0.0f);
			}
			//push_back adds new element to the end of the vector
			vertices.push_back(vertex);
		}

		//Process each mesh face to indices
		for (GLuint i = 0; i < mesh->mNumFaces; i++) {
			aiFace face = mesh->mFaces[i];
			//Retrieve/Store indices in vector
			for (GLuint j = 0; j < face.mNumIndices; j++) {
				indices.push_back(face.mIndices[j]);
			}
		}

		//Process materials
		if (mesh->mMaterialIndex >= 0) {
			aiMaterial* material = scene->mMaterials[mesh->mMaterialIndex];
			vector<Texture> diffuseMaps = this->loadMaterialTextures(material, aiTextureType_DIFFUSE, "texture_diffuse");
			textures.insert(textures.end(), diffuseMaps.begin(), diffuseMaps.end());
			vector<Texture> specularMaps = this->loadMaterialTextures(material, aiTextureType_SPECULAR, "texture_specular");
			textures.insert(textures.end(), specularMaps.begin(), specularMaps.end());
		}

		return Mesh(vertices, indices, textures);
	}
	
		//Check materials and load textures
		vector<Texture> loadMaterialTextures(aiMaterial *mat, aiTextureType type, string typeName){
			vector<Texture> textures;
			for (GLuint i = 0; i < mat->GetTextureCount(type); i++) {
				aiString str;
				mat->GetTexture(type, i, &str);
				GLboolean skip = false;

				for (GLuint j = 0; j < textures_loaded.size(); j++) {
					//Check if path = string
					if (textures_loaded[j].path == str) {
						textures.push_back(textures_loaded[j]);
						skip = true;
						break;
					}
				}

				if (!skip) {
					Texture texture;
					texture.id = TextureFromFile(str.C_Str(), this->directory);
					texture.type = typeName;
					texture.path = str.C_Str();
					textures.push_back(texture);
					this->textures_loaded.push_back(texture);
				}
			}
			return textures;
		}
};

GLint TextureFromFile(const char *path, string directory) {
	string filename = string(path);
	filename = directory + '/' + filename;
	GLuint textureID;
	glGenTextures(1, &textureID);

	//Bind/wrap texts and generate mip maps
	int width, height; 
	unsigned char *image =SOIL_load_image(filename.c_str(), &width, &height, 0, SOIL_LOAD_RGB);
	glBindTexture(GL_TEXTURE_2D, textureID);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
	glGenerateMipmap(GL_TEXTURE_2D);	
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR );
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glBindTexture(GL_TEXTURE_2D, 0);

	SOIL_free_image_data(image);
	return textureID;
}

