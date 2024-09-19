package com.clinicapaw.backend_clinicapaw.persistence.repository;

import com.clinicapaw.backend_clinicapaw.enums.RoleEnum;
import com.clinicapaw.backend_clinicapaw.persistence.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("SELECT r FROM RoleEntity r WHERE r.roleEnum IN :roleEnums")
    Set<RoleEntity> findRoleEntitiesByRoleEnumIn(@Param("roleEnums") List<String> roleEnums);

    @Query("SELECT r FROM RoleEntity r WHERE r.roleEnum = :roleEnum")
    Optional<RoleEntity> findByRoleEnum(@Param("roleEnum") RoleEnum roleEnum);
}
