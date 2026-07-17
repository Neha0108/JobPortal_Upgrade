package com.recruitment.platform.dto.recruiter;

import com.recruitment.platform.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toResponse(Company company);
}
