package com.bbenslimane.app.ws.controllers;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbenslimane.app.ws.responses.ErrorMessages;
import com.bbenslimane.app.ws.responses.UserResponse;
import com.bbenslimane.app.ws.services.UserService;
import com.bbenslimane.app.ws.services.imp.UserServiceImp;
import com.bbenslimane.app.ws.shard.dto.UserDto;
import com.bbenslimane.app.ws.exception.UserException;
import com.bbenslimane.app.ws.requests.UserRequest;

@RestController
@RequestMapping("/users")  //localhost:8080/users

public class UserController {
	
	
	
	@Autowired
	UserService userService;
	 
	
	
	@GetMapping(path="/{id}", produces={MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE}) // produces=MediaType.APPLICATION_XML_VALUE to receive XML response 
																		//consumes = MediaType.APPLICATION_XML_VALUE to send XML content via request
																		//produces={MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE} to receive XML or JSON response 
	public ResponseEntity<UserResponse>   getUser(@PathVariable String id) {
		
		UserDto userDto = userService.getUserByUserId(id);
		
		UserResponse userResponse = new UserResponse();
		
		BeanUtils.copyProperties(userDto, userResponse);
		
		return new ResponseEntity<>(userResponse, HttpStatus.OK) ;
		
		
	}
	
	@GetMapping(produces={MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE})
	public List<UserResponse> getUsers(@RequestParam(value="page", defaultValue="1") int page ,@RequestParam(value="limit", defaultValue="15") int limit ){
		
		List<UserResponse> usersResponse = new ArrayList<UserResponse>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for(UserDto userDto : users) {
			
			UserResponse user = new UserResponse();
			
			BeanUtils.copyProperties(userDto, user);
			
			usersResponse.add(user);			
		}
		
		return usersResponse;
		
		
		
	}
	
	
	//@PostMapping("/users")
	@PostMapping(
			produces={MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE},
			consumes = {MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE}
			)
	public ResponseEntity<UserResponse>  createUser(@RequestBody @Validated UserRequest userRequest) throws Exception {
		
		if(userRequest.getFirstName().isEmpty()) throw new UserException(ErrorMessages.Missing_REQUIRED_FIELD.getErrorMessage());
		
//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userRequest, userDto);

		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);

		
		UserDto  createUser = userService.createUser(userDto);
		
//		UserResponse userResponse = new UserResponse();
//		BeanUtils.copyProperties(createUser, userResponse);
		
		UserResponse userResponse = modelMapper.map(createUser, UserResponse.class);
		
		return  new ResponseEntity<>(userResponse, HttpStatus.CREATED) ;

	}

	
	@PutMapping(
				path="/{id}",
				produces={MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE},
				consumes = {MediaType.APPLICATION_JSON_VALUE,  MediaType.APPLICATION_XML_VALUE}
				)
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable String id) {

		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userRequest, userDto);
		
		UserDto  updateUser = userService.updateUser(id, userDto);

		UserResponse userResponse = new UserResponse();
		BeanUtils.copyProperties(updateUser, userResponse);
		
		return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED) ;
	}

	@DeleteMapping(path="/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable String id) {

		userService.deleteUser(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
	}


}
