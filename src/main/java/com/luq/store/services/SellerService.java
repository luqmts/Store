package com.luq.store.services;

import java.time.LocalDateTime;
import java.util.List;

import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.exceptions.InvalidMailException;
import com.luq.store.exceptions.InvalidPhoneException;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.repositories.SellerRepository;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sRepository;
    @Autowired
    private SellerMapper sMapper;
    
    public List<SellerResponseDTO> getAll() {
        return sMapper.toDTOList(sRepository.findAll());
    }

    public List<SellerResponseDTO> getAllSorted(String sortBy, String direction, String department, String name, String mail, String phone) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        
        if (department != null || name != null || mail != null || phone != null){
            return sMapper.toDTOList(sRepository.findByDepartmentAndNameAndMailAndPhone(sort, Department.getDepartment(department), name, mail, phone));
        }
        return sMapper.toDTOList(sRepository.findAll(sort));
    }

    public SellerResponseDTO getById(int id) {
        Seller seller = sRepository.findById(id).orElse(null);
        assert seller != null;
        return sMapper.toDTO(seller);
    }

    public SellerResponseDTO register(SellerRegisterDTO data) {
        if (data.mail().toString().isBlank()) throw new InvalidMailException("Invalid mail");
        if (data.phone().toString().isBlank()) throw new InvalidPhoneException("Invalid phone");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Seller seller = sMapper.toEntity(data);

        seller.setModifiedBy(authentication.getName());
        seller.setModified(LocalDateTime.now());
        seller.setCreatedBy(authentication.getName());
        seller.setCreated(LocalDateTime.now());

        seller = sRepository.save(seller);
        return sMapper.toDTO(seller);
    }

    public SellerResponseDTO update(int id, SellerUpdateDTO data) {
        if (data.mail().toString().isBlank()) throw new InvalidMailException("Invalid mail");
        if (data.phone().toString().isBlank()) throw new InvalidPhoneException("Invalid phone");

        Seller seller = sRepository.findById(id).orElse(null);

        if (seller == null) throw new NotFoundException("Seller not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        seller.setName(data.name());
        seller.setMail(data.mail());
        seller.setPhone(data.phone());
        seller.setDepartment(data.department());

        seller.setModifiedBy(authentication.getName());
        seller.setModified(LocalDateTime.now());

        return sMapper.toDTO(sRepository.save(seller));
    }

    public void delete(int id) {
        sRepository.deleteById(id);
    }
}
