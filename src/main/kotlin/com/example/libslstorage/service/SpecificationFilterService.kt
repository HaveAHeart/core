package com.example.libslstorage.service

import com.example.libslstorage.component.SpecificationFilterManager
import com.example.libslstorage.dto.specification.SpecificationPageRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.repository.SpecificationRepository
import com.querydsl.core.BooleanBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class SpecificationFilterService(
    private val specificationFilterManager: SpecificationFilterManager,
    private val specificationRepository: SpecificationRepository
) {

    @Value("\${specificationPageSize}")
    private var pageSize = 20

    fun getAvailableFilters(): Map<String, String> =
        specificationFilterManager.availableHandlers

    fun getPage(request: SpecificationPageRequest): Page<SpecificationEntity> {
        val predicateBuilder = BooleanBuilder()
        request.filters.mapNotNull { specificationFilterManager.handle(it) }
            .forEach { predicateBuilder.and(it) }
        val pageRequest = PageRequest.of(request.page, pageSize)
        return specificationRepository.findAll(predicateBuilder, pageRequest)
    }
}
