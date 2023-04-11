package com.in.board.controller;

import com.in.board.dto.BoardDTO;
import com.in.board.dto.CommentDTO;
import com.in.board.service.BoardService;
import com.in.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor // Lombok이 제공하는 어노테이션 중 하나로, 주로 생성자에서 필요한 인자를 자동으로 생성해주는 기능
@RequestMapping("/board") // /board로 시작하는 주소를 먼저 받음
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save") // `/board/save`
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException { // @ModelAttribute: 스프링 MVC에서 컨트롤러 메서드에서 요청 파라미터를 객체에 바인딩할 때 사용하는 어노테이션 중 하나
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) { // 데이터를 가져와야 할 때는 모델 객체를 사용한다.
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable) { // 경로상에 있는 데이터 값을 받아올 때는 @PathVariable 어노테이션 사용
        /*
         * 해당 게시글의 조회수를 하나 올리고
         * 게시글 데이터를 가져와서 detail.html에 출력
         * */

        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
//        return "redirect:/board/" + boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) { // @PageableDefault 는 Spring MVC에서 페이지를 나누고 정렬하는 데 사용]
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();


        // page 개수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 개수 3개
        // 총 페이지 개수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";
    }
}
