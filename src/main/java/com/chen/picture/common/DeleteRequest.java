package com.chen.picture.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -8588378645619735244L;
    /**
     * id
     */
    private Long id;
}
