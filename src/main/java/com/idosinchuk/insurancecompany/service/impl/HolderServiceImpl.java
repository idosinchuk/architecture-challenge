package com.idosinchuk.insurancecompany.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idosinchuk.insurancecompany.dto.HolderRequestDTO;
import com.idosinchuk.insurancecompany.dto.HolderResponseDTO;
import com.idosinchuk.insurancecompany.entity.HolderEntity;
import com.idosinchuk.insurancecompany.repository.HolderRepository;
import com.idosinchuk.insurancecompany.service.HolderService;

/**
 * Implementation for holder service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("HolderService")
public class HolderServiceImpl implements HolderService {

	@Autowired
	private HolderRepository holderRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	public Page<HolderResponseDTO> getAllHolders(Pageable pageable) {

		Page<HolderEntity> entityResponse = holderRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<HolderResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public HolderResponseDTO getHolder(int id) {

		HolderEntity entityResponse = holderRepository.findById(id);

		return modelMapper.map(entityResponse, HolderResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public HolderResponseDTO addHolder(HolderRequestDTO holderRequestDTO) {

		HolderEntity entityRequest = modelMapper.map(holderRequestDTO, HolderEntity.class);

		HolderEntity entityResponse = holderRepository.save(entityRequest);

		return modelMapper.map(entityResponse, HolderResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public HolderResponseDTO updateHolder(HolderRequestDTO holderRequestDTO) {

		HolderEntity entityRequest = modelMapper.map(holderRequestDTO, HolderEntity.class);

		HolderEntity entityResponse = holderRepository.save(entityRequest);

		return modelMapper.map(entityResponse, HolderResponseDTO.class);

	}
}
