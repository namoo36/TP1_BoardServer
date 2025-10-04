package com.namoo.boardserver.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Setter
@ToString
@Slf4j
@NoArgsConstructor
public class UserDTO {
    public boolean hasNullDataBeforeRegister(UserDTO userDTO) {
        System.out.println(userDTO.toString());
        return userDTO.getPassword() == null || userDTO.getUserId() == null || userDTO.getNickname() == null;
    }

    public enum Status{
        USER, ADMIN, DELETED
    }

    public UserDTO(String id, String password, String name, String phone, String address, Status status, Date createTime, Date updateTime, boolean isAdmin) {
        this.userId = id;
        this.password = password;
        this.nickname = name;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isAdmin = isAdmin;
    }


    private Integer id;
    private String userId;
    private String password;
    private String nickname;
    private boolean isAdmin;
    private Date createTime;
    private boolean isWithDraw;
    private Status status;
    private Date updateTime;

}
