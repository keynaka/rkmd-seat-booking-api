package com.rkmd.toki_no_nagare.dto.email;

import lombok.Getter;

@Getter
public class ImagesDto {

    /** Nombre a utilizar en MimeBodyPart. */
    private String headerName;

    /** Código a utiilzar en MimeBodyPart para referenciar en el template html. */
    private String headerValue;

    /** Ruta + nombre de la imagen. */
    private String attachFilePath;

    public ImagesDto(String headerValue, String attachFilePath){
        this.headerName = "Content-ID";
        this.headerValue = headerValue;
        this.attachFilePath = attachFilePath;
    }
}
