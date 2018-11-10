package com.wiwj.screen.mapper;

import java.util.LinkedHashMap;
import java.util.List;

public interface TableMapper {
    List<LinkedHashMap> execute(String sql);
}
