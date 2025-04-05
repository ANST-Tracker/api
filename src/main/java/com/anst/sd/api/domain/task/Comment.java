package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.BusinessEntity;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Accessors(chain = true)
public class Comment extends BusinessEntity {
    @Column(nullable = false)
    private String content;
    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private AbstractTask task;
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany(mappedBy = "comment")
    private List<Attachment> attachments;
}
