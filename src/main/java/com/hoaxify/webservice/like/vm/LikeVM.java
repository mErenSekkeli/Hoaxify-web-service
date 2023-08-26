package com.hoaxify.webservice.like.vm;

import lombok.Data;

import java.util.List;

@Data
public class LikeVM {

    private String username;
    private List<Long> hoaxIds;
}
