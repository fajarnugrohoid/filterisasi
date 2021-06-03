package com.filterisasi.filterisasi;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import junit.framework.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class PpdbOptionRepositoryTest {

    @Autowired
    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;

    private final static List<String> USER_ID_LIST = Arrays.asList("b2b1f340-cba2-11e8-ad5d-873445c542a2", "bd5dd3a4-cba2-11e8-9594-3356a2e7ef10");

    private static final Random RANDOM = new Random();

    /*
    @BeforeEach
    public void dataSetup() {
        PpdbOption transaction;
        for (int i = 0; i < 10; i++) {
            String requestId = UUID.randomUUID().toString();
            if (i % 2 == 0) {
                transaction = new Transaction(requestId, true, USER_ID_LIST.get(RANDOM.nextInt(2)), System.currentTimeMillis());
            } else {
                transaction = new Transaction(requestId, false, USER_ID_LIST.get(RANDOM.nextInt(2)), System.currentTimeMillis());
            }

            ppdbOptionLookupSchoolRepository.save(transaction);
        }
    }*/

    /*
    @Test
    public void findSuccessfullOperationsForUserWithCreatedDateLessThanNowTest() {
        long now = System.currentTimeMillis();
        String userId = USER_ID_LIST.get(RANDOM.nextInt(2));
        List<Transaction> resultsPage =  transactionRepository.findBySuccessIsTrueAndCreatedLessThanEqualAndUserIdOrderByCreatedDesc(now, userId, PageRequest.of(0, 5)).getContent();

        assertThat(resultsPage).isNotEmpty();
        assertThat(resultsPage).extracting("userId").allMatch(id -> Objects.equals(id, userId));
        assertThat(resultsPage).extracting("created").isSortedAccordingTo(Collections.reverseOrder());
        assertThat(resultsPage).extracting("created").first().matches(createdTimeStamp -> (Long)createdTimeStamp <= now);
        assertThat(resultsPage).extracting("success").allMatch(sucessfull -> (Boolean)sucessfull == true);
    } */

    @Test
    public void findSuccessfullOperationsForUserWithCreatedDateLessThanNowTest() {
        long now = System.currentTimeMillis();
        String userId = USER_ID_LIST.get(RANDOM.nextInt(2));

        String types[] = new String[] { "abk"};
        List<PpdbOption> resultsPage =  ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchoolByJalur(types);
        for (int i = 0; i <resultsPage.size() ; i++) {
            System.out.println("==>" + resultsPage.get(i).getName());


        }
        int x = ppdbOptionLookupSchoolRepository.updateQuotaPpdbOption(resultsPage);
        //assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
        //        .containsOnly("value");
        //assertThat(resultsPage).isNotEmpty();
    }
}