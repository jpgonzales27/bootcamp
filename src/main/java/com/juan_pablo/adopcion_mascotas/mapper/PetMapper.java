package com.juan_pablo.adopcion_mascotas.mapper;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetDTO;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface PetMapper {

    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    @Mapping(source = "petType.typeName", target = "typeName")
    GetPetDTO fromEntityToDto(Pet pet);

    @InheritInverseConfiguration
    Pet fromDtoToEntity (GetPetDTO getPetDTO);

    List<GetPetDTO> fromEntityListToDtoList ( List<Pet> petList);
    List<Pet> fromDtoListToEntityList ( List<GetPetDTO> petDtoList);
}

