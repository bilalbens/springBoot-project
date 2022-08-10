package com.bbenslimane.app.ws.services.imp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bbenslimane.app.ws.entities.AddressEntity;
import com.bbenslimane.app.ws.entities.UserEntity;
import com.bbenslimane.app.ws.repositories.UserRepository;
import com.bbenslimane.app.ws.responses.UserResponse;
import com.bbenslimane.app.ws.services.UserService;
import com.bbenslimane.app.ws.shard.Utils;
import com.bbenslimane.app.ws.shard.dto.AddressDto;
import com.bbenslimane.app.ws.shard.dto.UserDto;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	Utils util;

	@Override
public UserDto createUser(UserDto user) {
		
		UserEntity checkUser = userRepository.findByEmail(user.getEmail());
		
		if(checkUser != null) throw new RuntimeException("User Alrady Exists !");
		
		
		for(int i=0; i < user.getAddresses().size(); i++) {
			
			AddressDto address = user.getAddresses().get(i);
			address.setUser(user);
			address.setAddressId(util.generateStringId(30));
			user.getAddresses().set(i, address);
		}
		
		user.getContact().setContactId(util.generateStringId(30));
		user.getContact().setUser(user);
		
//		System.out.println(user.getContact().getContactId());
		
        ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		userEntity.setUserId(util.generateStringId(32));
		
		UserEntity newUser = userRepository.save(userEntity);
		
		UserDto userDto =  modelMapper.map(newUser, UserDto.class);
		
		return userDto;
	}
	


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override

	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);


		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {

		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UsernameNotFoundException(userId);

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);

		return userDto;
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException(userId);
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setSecondName(user.getSecondName());
		
		UserEntity updatedUser = userRepository.save(userEntity);

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(updatedUser, userDto);
		return userDto;

		

		
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException(userId);		
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit, String search, int status) {
		
		
		if(page > 0) page -= 1;
		
		
		List<UserDto> usersDto = new ArrayList<UserDto>();
		
		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> userPage;
		if(search.isEmpty()){
				userPage =  userRepository.findAllUsers(pageableRequest);
		}else{
				userPage =  userRepository.findAllUserByCriteria(pageableRequest, search , status);
		}

		List<UserEntity> users = userPage.getContent();
				
		for(UserEntity userEntity : users) {
					
					ModelMapper modelMapper = new ModelMapper();
					UserDto user = modelMapper.map(userEntity, UserDto.class);

					usersDto.add(user);			
				}
		
		return usersDto;
	}

}
