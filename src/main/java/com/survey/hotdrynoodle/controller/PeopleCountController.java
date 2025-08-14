package com.survey.hotdrynoodle.controller;

import com.survey.hotdrynoodle.entity.PeopleCount;
import com.survey.hotdrynoodle.repository.PeopleCountRepository;
import com.survey.hotdrynoodle.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor //  自动注入 final 字段
public class PeopleCountController {

    @Autowired
    private   SurveyService surveyService;
    @Autowired
    private   PeopleCountRepository repo;

    private static final List<String> AGE_GROUPS = List.of("儿童", "青年", "中老年");

    @GetMapping("/")
    public String index(@RequestParam(value = "date", required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        Model model) {
        if (date == null) date = LocalDate.now();

        // 初始化 5-10 点每小时每年龄段记录
        for (int hour = 5; hour <= 10; hour++) {
            for (String ageGroup : AGE_GROUPS) {
                int finalHour = hour;
                boolean exists = repo.findAllByDate(date).stream()
                        .anyMatch(p -> p.getHour() == finalHour && ageGroup.equals(p.getAgeGroup()));
                if (!exists) {
                    PeopleCount p = new PeopleCount();
                    p.setDate(date);
                    p.setHour(hour);
                    p.setAgeGroup(ageGroup);
                    p.setCount(0);
                    p.setGender("");
                    p.setLocation("");
                    p.setNotes("");
                    repo.save(p);
                }
            }
        }

        // 获取并排序
        List<PeopleCount> peopleList = repo.findAllByDate(date).stream()
                .filter(p -> p.getHour() >= 5 && p.getHour() <= 10)
                .sorted(Comparator.comparing(PeopleCount::getHour)
                        .thenComparing(PeopleCount::getAgeGroup))
                .toList();

        model.addAttribute("peopleList", peopleList);
        model.addAttribute("selectedDate", date);
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateCount(@PathVariable Long id,
                              @RequestParam int delta,
                              @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (delta > 0) {
            surveyService.increment(id);
        } else {
            surveyService.decrement(id);
        }
        return "redirect:/?date=" + date;
    }
}
