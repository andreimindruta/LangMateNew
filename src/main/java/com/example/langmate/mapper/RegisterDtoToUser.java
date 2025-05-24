package com.example.langmate.mapper;

import com.example.langmate.controller.payload.request.RegisterDto;
import com.example.langmate.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterDtoToUser {

    User registerDtoToUser(RegisterDto registerDto);
}
