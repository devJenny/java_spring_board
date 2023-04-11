package com.in.board.repository;

// JPA를 사용하여 데이터를 저장하고 조회하는 메서드를 정의

import com.in.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// JpaRepository는 Spring Data JPA에서 제공하는 인터페이스로, 기본적인 CRUD(Create, Read, Update, Delete) 메서드를 제공
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // update board_table set board_hits=board_hits+1 where id=?
    @Modifying // update나 delete 기능을 실행할 때는 @modifying 어노테이션 붙임
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") // b는 BoardEntity의 약어
    void updateHits(@Param("id") Long id);
}
