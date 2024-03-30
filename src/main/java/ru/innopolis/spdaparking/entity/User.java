package ru.innopolis.spdaparking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username")
    private String name;
    private String password;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Lesson> lessons = new ArrayList<>();

    @Fetch(FetchMode.JOIN)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "lesson", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "name")
    List<String> lessonNames = new ArrayList<>();
}
