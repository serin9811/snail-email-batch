package kr.snailemail.snailemailbatch.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    @Column(name = "email_sender_id")
    private Long emailSenderId;

    @Column(name = "email_title")
    private String emailTitle;

    @Column(name = "email_content")
    private String emailContent;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    @Column(name = "send_yn")
    private boolean sendYn;

    @Builder
    public Email(Long emailSenderId, String emailTitle, String emailContent, LocalDateTime createDate, LocalDateTime sendDate, boolean sendYn) {
        this.emailSenderId = emailSenderId;
        this.emailTitle = emailTitle;
        this.emailContent = emailContent;
        this.createDate = createDate;
        this.sendDate = sendDate;
        this.sendYn = sendYn;
    }
}
