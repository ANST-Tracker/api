package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.BusinessEntity;
import com.anst.sd.api.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attachment")
@Getter
@Setter
public class Attachment extends BusinessEntity {
    @Column(name = "file_id", nullable = false)
    private String fileId;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private AbstractTask task;
    @ManyToOne(optional = false)
    @JoinColumn(name = "uploader_id")
    private User uploader;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
