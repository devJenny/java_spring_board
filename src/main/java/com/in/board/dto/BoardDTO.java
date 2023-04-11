package com.in.board.dto;

import com.in.board.entity.BoardEntity;
import com.in.board.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// DTO(Data Transfer Object), VO, Bean,      Entity : 데이터 전송을 위한 객체
// 일반적으로 DTO는 데이터베이스 테이블과 매핑되는 필드와 게터/세터 메서드를 갖는 자바 클래스로 구현
// thymeleaf로 보내줌

@Getter
@Setter
@ToString
@NoArgsConstructor
// 기본생성자 : 인자를 가지지 않는 기본 생성자를 컴파일러가 자동으로 생성해줍니다. 이를 통해 코드를 간결하게 작성할 수 있으며, JPA에서 엔티티 클래스를 정의할 때도 유용하게 사용됩니다.
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자 : 클래스의 모든 필드를 인자로 받는 생성자를 자동으로 생성해주는 역할을 합니다.
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // sava.html -> Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        if (boardEntity.getFileAttached() == 0) {
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            // 파일 이름을 가져가야 함
            // originalFileName, storedFileName: board_file_table(BoardFileEntity)
            // join
            // select * form board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.1d=?
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
//                boardDTO.setOriginalFileName(boardEntity.getBoardFileEntityList().get(0).getOriginalFileName()); // get 인덱스 // 첨부파일이 여러 개이면 반복문을 돌려야 함
//                boardDTO.setStoredFileName(boardEntity.getBoardFileEntityList().get(0).getStoredFileName());
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }
        return boardDTO;
    }
}
