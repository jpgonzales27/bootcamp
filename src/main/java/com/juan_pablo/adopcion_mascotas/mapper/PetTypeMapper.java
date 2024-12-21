package com.juan_pablo.adopcion_mascotas.mapper;

import com.juan_pablo.adopcion_mascotas.dto.response.GetPetTypeDTO;
import com.juan_pablo.adopcion_mascotas.persistence.entity.PetType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PetTypeMapper {

    GetPetTypeDTO fromEntityToDto(PetType petType);

    @InheritInverseConfiguration
    PetType fromDtoToEntity(GetPetTypeDTO getPetType);

    List<GetPetTypeDTO> fromEntityListToDtoList(List<PetType> productList);

    List<PetType> fromDtoListToEntityList(List<GetPetTypeDTO> productDtoList);
}
