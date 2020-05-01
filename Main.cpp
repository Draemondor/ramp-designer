//Dependencies
#include <glad\glad.h>
#include <GLFW\glfw3.h>
#include <SOIL2.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>
//Std libs
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <iostream>
#include <stdlib.h>
#include <vector>
#include <string>
#include <fstream>
#include <stdio.h>
//External libs
#include "shader.h"
#include "camera.h"
#include "shader.h"
#include "model.h"


//Functions
void frameBufferSize(GLFWwindow* window, int width, int height);
void inputs(GLFWwindow* window);
void mouse_movement(GLFWwindow* window, double x_pos, double y_pos);
void Zoom(GLFWwindow* window, double x_offset, double y_offset);

//Window Dimensions
const unsigned int screen_width = 1280;
const unsigned int screen_height = 720;

//Camera settings
Camera cam(glm::vec3(0.0f, 3.0f, 15.0f));
float x_prev = (float)screen_width / 2.0f;
float y_prev = (float)screen_height / 2.0f;
bool init_mouse = true;

//Lighting position
glm::vec3 light_position(sin(glfwGetTime() *1.2f), 15.0f, 2.0f);

//Movement timing
float deltaTime = 0.0f;
float lastFrame = 0.0f;


//-----------------------Main------------------------//
int main() {
	//Initialize GLFW with corresponding openGL verison--> [OpenGL v3.3]{lowest version possible that supports libraries used}
	glfwInit();
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);	

	//Create window w/ specified Dimensions & attributes & error checking
	GLFWwindow* window = glfwCreateWindow(screen_width, screen_height, "Snek-engine PRE-ALPHA", NULL, NULL);
	if (window == NULL) {
		std::cout << "GLFW failed" << std::endl;
		glfwTerminate();
		return -1;
	}

	//GLFW Callbacks
	glfwMakeContextCurrent(window);
	glfwSetFramebufferSizeCallback(window, frameBufferSize);
	glfwSetCursorPosCallback(window, mouse_movement);
	glfwSetScrollCallback(window, Zoom);
	glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	//Load Glad pointers
	if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress)) {
		std::cout << "Glad initialization failed" << std::endl;
		return -1;
	}

	//Configure global openGL state
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_BLEND);
	glDepthFunc(GL_LESS);

	//******Build/compile shader program/s******//
	//Ex. -> Shader myShader("vs.shader", "fs.shader");
	Shader lighting("obj_vs.shader", "obj_fs.shader");
	//Shader lightSource("lightsrc_vs.shader", "lightsrc_fs.shader");
	Shader modelShader("mod_vs.shader", "mod_fs.shader");
	//****************************************//
	
	//************Load Models**********//
	//Model new_model("resources/models/Z3_OBJ/Z3_OBJ.obj");
	//Model new_model("resources/models/WoodenCabin/WoodenCabinObj.obj");
	//Model new_model("resources/models/tank/tank.obj");
	//Model new_model("resources/models/plane/plane.obj");
	Model new_model("resources/models/ramp/ramp.obj");
	//Model new_model("resources/models/cube/cube.obj");
	//---Model Scale---//
	float scale = .10f;
	//*********************************//
	
	//*************Game/Render loop***************//
	while (!glfwWindowShouldClose(window)) {
		//Timing per frame
		float currentFrame = glfwGetTime();
		deltaTime = currentFrame - lastFrame;
		lastFrame = currentFrame;
		
		//Inputs
		inputs(window);

		//Render
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//Set projection view matrix to cam view
		glm::mat4 projection = glm::perspective(glm::radians(cam.zoom), (float)screen_width / (float)screen_height, 0.1f, 100.0f);
		glm::mat4 view = cam.GetViewMatrix();
		
		/***********Shader implementation************
		//Exx. -> Send uniform locations to shader
		//myShader.setMat4("model", model);
		//myShader.setMat4("view", view);
		//myShader.setMat4("projection", projection);
		*********************************************/

		//Activate Object shaders 
		lighting.use();
		lighting.setVec3("objectColor", .40f, 0.5f, 0.91f);
		lighting.setVec3("lightColor", 1.0f, 1.0f, 1.0f);
		lighting.setVec3("lightPosition", light_position);
		lighting.setVec3("viewPosition", cam.position);
		lighting.setMat4("projection", projection);
		lighting.setMat4("view", view);

		////---Object Loading/Transformations-----//
		////Init model matrix with identity matrix first//
		glm::mat4 model = glm::mat4(1.0f);;		
		//model = glm::rotate(model,((float)glfwGetTime()*1.0f)*3.0f, glm::vec3(0.0f, 0.50f, 0.50f));
		model = glm::translate(model, glm::vec3(0.0f,-1.20f,-2.0f));
		model = glm::scale(model, glm::vec3(scale));
		lighting.setMat4("model", model);
		//--------------------------------------//
		

		//Light Source Movement
		light_position.x = 10.0f + cos(glfwGetTime()) * 50.0f;
		light_position.y = 10.0f + cos(glfwGetTime() / 2.0f) * 1.0f;
		
		//Render Light Source
		/*lightSource.use();
		lightSource.setMat4("projection", projection);
		lightSource.setMat4("view", view);
		model = glm::mat4(1.0f);
		model = glm::translate(model, light_position);
		model = glm::scale(model, glm::vec3(0.3f) * 2.0f * sin(lastFrame * sin((float)glfwGetTime() / 20.0f)));
		model = glm::rotate_slow(model, (float)glfwGetTime() * 0.90f, glm::vec3(1.0f, 1.0f, 0.0f));
		lightSource.setMat4("model", model);*/


		//Activate Model Shaders
		modelShader.use();
		modelShader.setMat4("projection", projection);
		modelShader.setMat4("view", view);
		modelShader.setMat4("model", model);
		new_model.Draw(modelShader);
	
		//GLFW swap buffers and process IO actions
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	//Clear GLFW allocated resources
	glfwTerminate();
	return 0;
}
//********************************************//

//----------------------Functions--------------------------//
//Window re-size
void frameBufferSize(GLFWwindow* window, int width, int height) {
	glViewport(0, 0, width, height);
}

//Detects inputs of key presses/releases
void inputs(GLFWwindow* window) {
	if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
		glfwSetWindowShouldClose(window, true);
	}
	if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
		cam.KeyPress(FRONT, deltaTime);
	}
	if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
		cam.KeyPress(BACK, deltaTime);
	}
	if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
		cam.KeyPress(LEFT, deltaTime);
	}
	if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
		cam.KeyPress(RIGHT, deltaTime);
	}
	if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
		cam.KeyPress(UP, deltaTime);
	}
	if (glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS) {
		cam.KeyPress(DOWN, deltaTime);
	}
	//Wireframe switch
	if (glfwGetKey(window, GLFW_KEY_F) == GLFW_PRESS) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	if (glfwGetKey(window, GLFW_KEY_V) == GLFW_PRESS) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}

//Mouse movement
void mouse_movement(GLFWwindow* window, double x_pos, double y_pos) {
	if (init_mouse) {
		x_prev = x_pos;
		y_prev = y_pos;
		init_mouse = false;
	}
	float x_offset = x_pos - x_prev;
	float y_offset = y_prev - y_pos; //<--y starts at bottom of the screen
	x_prev = x_pos;
	y_prev = y_pos;

	cam.MouseInput(x_offset, y_offset);
}

//Controls FOV(field of view) zoom using scroll-wheel
void Zoom(GLFWwindow* window, double x_offset, double y_offset) {
	cam.ZoomScroll(y_offset);
}
//---------------------------------------------------------//
