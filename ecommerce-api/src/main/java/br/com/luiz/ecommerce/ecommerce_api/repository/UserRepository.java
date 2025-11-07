package br.com.luiz.ecommerce.ecommerce_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.luiz.ecommerce.ecommerce_api.domain.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long> {


}
