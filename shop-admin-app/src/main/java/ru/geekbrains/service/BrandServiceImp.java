package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.BrandDto;
import ru.geekbrains.controller.BrandListParam;
import ru.geekbrains.persist.BrandRepository;
import ru.geekbrains.persist.BrandSpecification;
import ru.geekbrains.persist.CategorySpecification;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImp implements BrandService {

    private BrandRepository brandRepository;

    @Autowired
    public BrandServiceImp(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Page<Brand> findWithFilter(BrandListParam listParam) {
        Specification<Brand> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(BrandSpecification.titlePrefix(listParam.getTitleFilter()));
        }

        String sortField = "id";
        if (listParam.getSortField() != null && !listParam.getSortField().isBlank()) {
            sortField = listParam.getSortField();
        }

        Sort sort = Sort.by(sortField).ascending();
        if ("desc".equals(listParam.getSortDirection())) {
            sort = sort.descending();
        }

        return brandRepository.findAll(specification,
                PageRequest.of(Optional.ofNullable(listParam.getPage()).orElse(1) - 1,
                        Optional.ofNullable(listParam.getSize()).orElse(5), sort));
    }

    @Override
    public void save(BrandDto brandDto) {
        brandRepository.save(new Brand(brandDto.getId(), brandDto.getTitle()));
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }
}
