package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tnews.entity.KeyWord;
import tnews.repository.KeyWordRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class KeyWordsService {
    private KeyWordRepository keyWordRepository;

    public List<KeyWord> getAllKeyWords() {
        return keyWordRepository.findAll();
    }

    public KeyWord getKeyWordById(Long id) {
        return keyWordRepository.findById(id).orElse(null);
    }

    public KeyWord saveKeyWord(KeyWord keyWord) {
        return keyWordRepository.save(keyWord);
    }



}
