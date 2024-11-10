package com.juan_pablo.adopcion_mascotas.service.Impl;

import com.juan_pablo.adopcion_mascotas.exception.ObjectNotFoundException;
import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import com.juan_pablo.adopcion_mascotas.persistence.repository.AdoptionCrudRepository;
import com.juan_pablo.adopcion_mascotas.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdoptionServiceImpl implements AdoptionService {

    @Autowired
    private AdoptionCrudRepository adoptionCrudRepository;

    @Override
    public List<Adoption> findAllAdoptions() {
        return adoptionCrudRepository.findAll();
    }

    @Override
    public Adoption findAdoptionById(Long id) {
        return adoptionCrudRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("[Adoption: " + Long.toString(id) + "]"));
    }

    @Override
    public Adoption saveAdoption(Adoption adoption) {
        return adoptionCrudRepository.save(adoption);
    }

    @Override
    public Adoption updateAdoptionById(Long id, Adoption adoption) {
        Adoption oldAdoption = this.findAdoptionById(id);
        oldAdoption.setPet(adoption.getPet());
        oldAdoption.setUser(adoption.getUser());
        oldAdoption.setAdoptionDate(adoption.getAdoptionDate());
        return adoptionCrudRepository.save(oldAdoption);
    }

    @Override
    public void deleteAdoptionById(Long id) {
        Adoption exist = this.findAdoptionById(id);
        adoptionCrudRepository.delete(exist);
    }
}
