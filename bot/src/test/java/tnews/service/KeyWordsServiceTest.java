package tnews.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tnews.entity.KeyWord;
import tnews.repository.KeyWordRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class KeyWordsServiceTest {
    @Autowired
    private KeyWordsService keyWordsService;
    @Autowired
    private KeyWordRepository keyWordRepository;

    Long KeyWordId1;
    Long KeyWordId2;
    Long KeyWordId3;

    @BeforeEach
    public void setUp() {
        KeyWord KeyWord1 = new KeyWord(null, "ONE", Set.of());
        KeyWord KeyWord2 = new KeyWord(null, "TWO", Set.of());
        KeyWord KeyWord3 = new KeyWord(null, "THREE", Set.of());

        KeyWord savedKeyWord = keyWordRepository.save(KeyWord1);
        KeyWord savedKeyWord2 = keyWordRepository.save(KeyWord2);
        KeyWord savedKeyWord3 = keyWordRepository.save(KeyWord3);

        KeyWordId1 = savedKeyWord.getId();
        KeyWordId2 = savedKeyWord2.getId();
        KeyWordId3 = savedKeyWord3.getId();
    }

    @AfterEach
    public void tearDown() {
        keyWordRepository.deleteById(KeyWordId1);
        keyWordRepository.deleteById(KeyWordId2);
        keyWordRepository.deleteById(KeyWordId3);
    }

    @Test
    void saveKeyWord() {
        KeyWord KeyWord = new KeyWord(null, "NEW", Set.of());
        KeyWord savedKeyWord = keyWordsService.saveKeyWord(KeyWord);
        assertNotNull(savedKeyWord);
        KeyWord retrievedKeyWord = keyWordRepository.findById(savedKeyWord.getId()).orElse(null);
        assertNotNull(retrievedKeyWord);
        assertEquals(savedKeyWord.getId(), retrievedKeyWord.getId());
        assertEquals(savedKeyWord.getKeyword(), retrievedKeyWord.getKeyword());
        Long id = retrievedKeyWord.getId();
        keyWordRepository.deleteById(id);
        assertNull(keyWordRepository.findById(id).orElse(null));
    }

    @Test
    void getAllKeyWords() {
        List<KeyWord> keyWords = keyWordsService.getAllKeyWords();
        List<KeyWord> retrievedKeyWords = keyWordRepository.findAll();
        assertNotNull(keyWords);
        assertNotNull(retrievedKeyWords);
        assertEquals(keyWords.size(), retrievedKeyWords.size());
        for (int i = 0; i < keyWords.size(); i++) {
            assertEquals(keyWords.get(i).getKeyword(), retrievedKeyWords.get(i).getKeyword());
        }
        KeyWord KeyWord = new KeyWord(null, "ONE", Set.of());
        keyWordsService.saveKeyWord(KeyWord);
        List<KeyWord> keyWords1 = keyWordsService.getAllKeyWords();
        assertNotNull(keyWords1);
        assertEquals(keyWords1.size(), keyWords.size());
    }

    @Test
    void getKeyWordById() {
        KeyWord keyWord = keyWordsService.getKeyWordById(KeyWordId1);
        assertNotNull(keyWord);
        KeyWord retrievedKeyWord = keyWordRepository.findById(keyWord.getId()).orElse(null);
        assertNotNull(retrievedKeyWord);
        assertEquals(keyWord.getId(), retrievedKeyWord.getId());
        assertEquals(keyWord.getKeyword(), retrievedKeyWord.getKeyword());
    }

    @Test
    void updateKeyWord() {

    }
}