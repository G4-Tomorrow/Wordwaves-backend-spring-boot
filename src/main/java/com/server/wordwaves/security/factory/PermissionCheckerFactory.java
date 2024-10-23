package com.server.wordwaves.security.factory;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.server.wordwaves.constant.PermissionType;
import com.server.wordwaves.security.checker.PermissionChecker;
import com.server.wordwaves.security.checker.implement.TopicPermissionChecker;
import com.server.wordwaves.security.checker.implement.WordCollectionPermissionChecker;
import com.server.wordwaves.security.checker.implement.WordPermissionChecker;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionCheckerFactory {

    Map<PermissionType, PermissionChecker> checkerMap;

    public PermissionCheckerFactory(
            WordCollectionPermissionChecker wordCollectionChecker,
            TopicPermissionChecker topicChecker,
            WordPermissionChecker wordChecker) {

        checkerMap = new EnumMap<>(PermissionType.class);
        checkerMap.put(PermissionType.COLLECTION, wordCollectionChecker);
        checkerMap.put(PermissionType.TOPIC, topicChecker);
        checkerMap.put(PermissionType.WORD, wordChecker);
    }

    public PermissionChecker getPermissionChecker(PermissionType type) {
        if (!checkerMap.containsKey(type)) {
            throw new IllegalArgumentException("Invalid PermissionType: " + type);
        }
        return checkerMap.get(type);
    }
}
