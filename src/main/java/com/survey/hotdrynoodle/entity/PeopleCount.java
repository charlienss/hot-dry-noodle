package com.survey.hotdrynoodle.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "people_count")
public class PeopleCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hour; // 小时 0-23
    private String ageGroup; // 年龄段
    private int count;
    private LocalDate date; // 日期

    // 新增字段
    private String gender; // 性别，如 "男"/"女"
    private String location; // 门店位置或编号
    private String notes; // 备注

    @Version
    private Integer version; //  用于乐观锁


}
