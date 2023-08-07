package com.agendaCraft.agendaCraft.repository;

import java.util.Optional;

import com.agendaCraft.agendaCraft.enums.EnumRole;
import com.agendaCraft.agendaCraft.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(EnumRole name);
}