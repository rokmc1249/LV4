package com.nakta.springlv1.domain.board.service;

import com.nakta.springlv1.domain.board.dto.BoardRequestDto;
import com.nakta.springlv1.domain.board.dto.BoardResponseDto;
import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.board.repository.BoardRepository;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.domain.user.entity.User;
import com.nakta.springlv1.domain.user.jwt.JwtUtil;
import com.nakta.springlv1.domain.user.jwt.UserRoleEnum;
import com.nakta.springlv1.global.exception.CustomException;
import com.nakta.springlv1.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {


        Board board = new Board(requestDto, user); //username을 따로 받기 위한 생성자 생성
        Board newboard = boardRepository.save(board);
        return new BoardResponseDto(newboard);
    }

    public List<BoardResponseDto> getAllBoard() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }
    public BoardResponseDto getOneBoard(Long id) {
        Board board = findById(id);
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto modifyBoard(Long id, BoardRequestDto requestDto, User user) {



        //작성자 일치 확인
        Board board = findById(id);



        if (user.getRole().equals(UserRoleEnum.ADMIN)||board.getUsername().equals(user.getUsername())) {
            board.update(requestDto);
            return new BoardResponseDto(board);
        } else {
            throw new CustomException(ErrorCode.ID_NOT_MATCH);
        }
    }

    public StringResponseDto deleteBoard(Long id, User user) {

        //작성자 일치 확인
        Board board = findById(id);

        if (user.getRole().equals(UserRoleEnum.ADMIN)||board.getUsername().equals(user.getUsername())) {
            boardRepository.deleteById(id);
            return new StringResponseDto("삭제를 성공하였음");
        } else {
            throw new CustomException(ErrorCode.ID_NOT_MATCH);
        }

    }

    private Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }


}