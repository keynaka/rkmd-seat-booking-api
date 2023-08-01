package com.rkmd.toki_no_nagare.utils;

import com.rkmd.toki_no_nagare.exception.BadRequestException;
import com.rkmd.toki_no_nagare.exception.NotFoundException;

public class ValidationUtils {
    public static void checkParam(boolean condition, String code, String description) {
        if (!condition)
            throw new BadRequestException(code, description);
    }

    public static void checkFound(boolean condition, String code, String description) {
        if (!condition)
            throw new NotFoundException(code, description);
    }
}
