package com.codewithmosh.store.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
     UserDto userToUserDto(User user);

     User  toEntity (RegisterUserRequest request);

     void updateUser(UpdateUserRequest request,@MappingTarget User user);

}