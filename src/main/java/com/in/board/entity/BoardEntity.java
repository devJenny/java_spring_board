package com.in.board.entity;
// DB 테이블 역할을 하는 클래스
// Entity는 객체 지향 프로그래밍에서 도메인을 나타내는 클래스입니다. 일반적으로 데이터베이스의 테이블과 매핑되어 사용됩니다.
// Entity 클래스는 데이터베이스의 테이블과 매핑되기 때문에, 클래스의 필드는 테이블의 컬럼과 일치해야 합니다. 또한, Entity 클래스는 JPA(Java Persistence API)에서 사용하는 애노테이션을 이용하여 매핑 정보를 지정할 수 있습니다.

import com.in.board.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table") // 매핑될 데이터베이스 테이블의 이름을 지정
public class BoardEntity extends BaseEntity{
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment , Primary Key 값의 생성 방식을 지정
    private Long id;

    // @Column: 매핑될 데이터베이스 테이블의 컬럼과 매핑될 클래스의 필드를 지정
    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // 크기 255, null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; // 1 or 0

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(0); // 파일 없음
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        boardEntity.setFileAttached(1); // 파일 있음
        return boardEntity;
    }
}
