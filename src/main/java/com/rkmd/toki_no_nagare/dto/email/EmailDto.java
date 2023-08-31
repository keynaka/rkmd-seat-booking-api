package com.rkmd.toki_no_nagare.dto.email;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
    private String htmlBody;

    /** Archivo adjunto */
    private String attachment;

    /** Listado de imagenes. */
    private Map<String, ImagesDto> imagesDataMap;

    /** Instancia un EmailDto sin archivo adjunto. */
    public EmailDto(String recipient, String msgBody, String subject){
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }

    public EmailDto(String recipient, String htmlBody, String subject, Map<String, ImagesDto> imagesDataMap){
        this.recipient = recipient;
        this.htmlBody = htmlBody;
        this.subject = subject;
        this.imagesDataMap = imagesDataMap;
    }
}
