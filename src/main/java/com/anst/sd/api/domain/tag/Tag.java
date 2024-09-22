package com.anst.sd.api.domain.tag;

import com.anst.sd.api.domain.DomainObject;
import com.anst.sd.api.domain.task.Task;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Table(name = "tag")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
public class Tag extends DomainObject {
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String color;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;
}
