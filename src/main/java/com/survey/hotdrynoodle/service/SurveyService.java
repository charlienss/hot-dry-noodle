package com.survey.hotdrynoodle.service;

import com.survey.hotdrynoodle.entity.PeopleCount;
import com.survey.hotdrynoodle.repository.PeopleCountRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyService {

    private final PeopleCountRepository repo;

    public SurveyService(PeopleCountRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void increment(Long id) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                PeopleCount p = repo.findById(id).orElseThrow();
                p.setCount(p.getCount() + 1);
                repo.save(p);
                return;
            } catch (OptimisticLockException e) {
                if (attempt == 2) throw e; // 最多重试 3 次
            }
        }
    }

    @Transactional
    public void decrement(Long id) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                PeopleCount p = repo.findById(id).orElseThrow();
                if (p.getCount() > 0) {
                    p.setCount(p.getCount() - 1);
                    repo.save(p);
                }
                return;
            } catch (OptimisticLockException e) {
                if (attempt == 2) throw e;
            }
        }
    }
}
