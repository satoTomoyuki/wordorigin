package com.wordOrigin.dto;

import com.wordOrigin.entity.F01Word;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class TestCommonDto implements Serializable {
    //テストNo（テスト単語リストのインデックス＋1）
    private int testIndex;

    //対象とした語源ID
    private int wordOriginId;

    //テスト単語リスト
    private List<F01Word> wordList;
}

