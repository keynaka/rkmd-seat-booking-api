package com.rkmd.toki_no_nagare.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EmailDto {

    /** Destinatario */
    private String recipient;

    /** Mensaje */
    private String msgBody;

    /** Asunto */
    private String subject;

    /** Cuerpo del mensaje en formato html */
    @Setter
    private String htmlContent;

    /** Archivo adjunto */
    private String attachment;

    /** Instancia un EmailDto sin archivo adjunto. */
    public EmailDto(String recipient, String msgBody, String subject){
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }
}
