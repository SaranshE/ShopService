package com.cogent.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cogent.main.entity.TokenEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer>
{
	@Query(value = "from TokenEntity te inner join UserEntity ue on te.userEntity.id = ue.id where ue.id = :userId and (te.valid = true)")
	List<TokenEntity> findAllValidTokenByUserEntity(int userId);

	Optional<TokenEntity> findByToken(String jwtToken);
}
