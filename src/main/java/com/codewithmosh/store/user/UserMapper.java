package com.codewithmosh.store.user;

import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
     UserDto userToUserDto(User user);

     User  toEntity (RegisterUserRequest request);

     void updateUser(UpdateUserRequest request,@MappingTarget User user);

}