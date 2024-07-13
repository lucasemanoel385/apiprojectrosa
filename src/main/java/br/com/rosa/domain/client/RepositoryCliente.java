package br.com.rosa.domain.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryCliente extends JpaRepository<Client, Long> {

    boolean existsByCpfCnpj(String cpf);

    boolean existsByRgStateRegistration(String rg);

    @Query(value = "select * from client where name_reason like :search% or cpf_cnpj like :search%", nativeQuery = true)
    Page<Client> findAllByNameRasonAndcpfCnpj(String search, Pageable page);

    @Query(value = "select * from client", nativeQuery = true)
    Page<Client> findAll(Pageable page);

}
