package com.tinyhuman.tinyhumanapi.common.utils;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {

}
