package com.teamproject.TP_backend.repository;

import com.teamproject.TP_backend.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {

}
