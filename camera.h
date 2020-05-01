#pragma once
//Dependencies
#include <glad/glad.h>
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <vector>

enum cam_movement {
	FRONT,
	BACK,
	LEFT,
	RIGHT,
	UP,
	DOWN,
};

//Default settings
const float YAW = -90.0f;
const float PITCH = 0.0f;
const float SPEED = 6.5f;
const float SENSITIVITY = 0.1f;
const float FOV = 45.0f;

class Camera {
	
public:
	//Initialize cam attributes
	glm::vec3 position;
	glm::vec3 front;
	glm::vec3 up;
	glm::vec3 down;
	glm::vec3 right;
	glm::vec3 world_up;

	float move_speed;
	float mouse_sens;
	float zoom;

	//Euler Angles
	float e_yaw;
	float e_pitch;

	//Constructor --> vectors
	Camera(glm::vec3 pos = glm::vec3(0.0f, 0.0f, 0.0f),
		glm::vec3 up = glm::vec3(0.0f, 1.0f, 0.0f),
		float yaw = YAW, float pitch = PITCH) : front(glm::vec3(0.0f, 0.0f, -1.0f)),
		move_speed(SPEED), mouse_sens(SENSITIVITY), zoom(FOV) {

		position = pos;
		world_up = up;
		e_pitch = pitch;
		e_yaw = yaw;
		updateCamVecs();
	}

	//View matrix using Euler angles and LookAt matrix
	glm::mat4 GetViewMatrix() {
		return glm::lookAt(position, position + front, up);
	}

	//Process keyboard inputs
	void KeyPress(cam_movement direction, float deltaTime) {
		float velocity = move_speed * deltaTime;
		if (direction == FRONT) {
			position += front * velocity;
		}
		if (direction == BACK) {
			position -= front * velocity;
		}
		if (direction == LEFT) {
			position -= right * velocity;
		}
		if (direction == RIGHT) {
			position += right * velocity;
		}
		if (direction == UP) {
			position += up * velocity;
		}
		if (direction == DOWN) {
			position -= up * velocity;
		}
	}

	//Process mouse input
	void MouseInput(float x_offset, float y_offset, GLboolean constrainPitch = true){
		x_offset *= mouse_sens;
		y_offset *= mouse_sens;
		e_yaw += x_offset;
		e_pitch += y_offset;

		//Screen flip prevention for out of bounds pitch value
		if (constrainPitch) {
			if (e_pitch > 89.0f) {
				e_pitch = 89.0f;
			}
			if (e_pitch < -89.0f) {
				e_pitch = -89.0f;
			}
		}
		updateCamVecs();
	}

	//Process scroll-wheel input for zooming
	void ZoomScroll(float y_offset) {
		if (zoom >= 1.0f && zoom <= FOV) {
			zoom -= y_offset;
		}
		if (zoom <= 1.0f) {
			zoom = 1.0f;
		}
		if (zoom >= FOV) {
			zoom = FOV;
		}
	}

private:
	//Gets front vec from Euler angles
	void updateCamVecs() {
		//Get new front vec
		glm::vec3 new_front;
		new_front.x = cos(glm::radians(e_yaw)) * cos(glm::radians(e_pitch));
		new_front.y = sin(glm::radians(e_pitch));
		new_front.z = sin(glm::radians(e_yaw)) * cos(glm::radians(e_pitch));
		front = glm::normalize(new_front);
		//Re-calculate vecs
		right = glm::normalize(glm::cross(front, world_up));
		up = glm::normalize(glm::cross(right, front));
	}
};

