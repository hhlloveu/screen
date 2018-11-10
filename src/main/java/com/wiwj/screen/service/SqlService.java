package com.wiwj.screen.service;

import java.util.LinkedHashMap;
import java.util.List;

public interface SqlService {
    List<LinkedHashMap> execute(String sql);
}
