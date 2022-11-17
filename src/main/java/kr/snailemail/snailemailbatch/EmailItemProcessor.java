package kr.snailemail.snailemailbatch;

import kr.snailemail.snailemailbatch.entity.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

@Slf4j
public class EmailItemProcessor implements ItemProcessor<Email, Email> {

    @Override
    public Email process(final Email item) throws Exception {
        final Long emailSenderId = item.getEmailSenderId();
        final String emailTitle = item.getEmailTitle();
        final String emailContent = item.getEmailContent();
        final LocalDateTime createDate = item.getCreateDate();
        final LocalDateTime sendDate = item.getSendDate();
        final boolean sendYn = item.isSendYn();

        final Email transformedEmail = Email.builder()
                .emailSenderId(emailSenderId)
                .emailTitle(emailTitle)
                .emailContent(emailContent)
                .createDate(createDate)
                .sendDate(sendDate)
                .sendYn(sendYn)
                .build();

        log.info("Converting (" + item + ") into ( " + transformedEmail + ")");

        return transformedEmail;
    }
}
