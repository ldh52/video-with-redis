package com.example.mytv.adapter.out.jpa.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserJpaRepository extends CrudRepository<UserJpaEntity, String> {
}
