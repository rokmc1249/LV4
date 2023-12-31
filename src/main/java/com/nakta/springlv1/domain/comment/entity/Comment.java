package com.nakta.springlv1.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nakta.springlv1.domain.board.entity.Board;
import com.nakta.springlv1.domain.comment.dto.CommentRequestDto;
import com.nakta.springlv1.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter

@NoArgsConstructor //지우기
public class Comment extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "username", length = 500)
    private String username;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Board board;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Comment(CommentRequestDto requestDto,Board board,User user){
        this.contents = requestDto.getContents();
        this.board = board;
        this.username = user.getUsername();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }


}
