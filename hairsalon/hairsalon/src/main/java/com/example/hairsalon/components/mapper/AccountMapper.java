package com.example.hairsalon.components.mapper;

import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.requests.AccountRequest.AccountUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
//    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
//    UserEntity toModel(CreateAccountRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonalFromRequest(AccountUpdateRequest updateRequest, @MappingTarget AccountEntity account);


    // Admin
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "roleId", target = "role_id.id")
//    void updateUserFromRequest(UpdateUserRequest updateRequest, @MappingTarget UserEntity user);
}