package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
// 양방향에서 ToString을 그냥 사용하면 상호 무한호출이 일어나 스택 오버플로우가 발생하므로
// 한쪽 엔터티에서는 ToString을 만들 때 제외시켜야한다.
@ToString(exclude = {"employees"})
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_dept")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private long id;


    @Column(name = "dept_name", nullable = false)
    private String name;

    /*
            양방향 매핑은 데이터베이스와 달리 객체지향 시스템에서 가능한 방법으로
            1대N관계에서 1쪽에 N데이터를 포함시킬 수 있는 방법이다.

            - 양방향 매핑에서 1쪽은 상대방 엔터티 갱신에 관여할 수 없고
              (리스트에서 사원을 지운다고 실제 디비에서 사원이 삭제되지는 않는다는 말)
              단순히 읽기전용 (조회전용)으로만 사용하는 것이다.

            - mappedBy에는 상대방 엔터티의 @ManyToOne에 대응되는 필드명을 꼭 적어야함
         */
    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();
}
