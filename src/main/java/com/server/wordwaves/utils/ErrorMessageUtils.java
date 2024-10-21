package com.server.wordwaves.utils;

import java.util.List;

public class ErrorMessageUtils {

    public static String generateUpdateRoleMessage(String roleName,
                                                   List<String> addedPermissions,
                                                   List<String> notAddedPermissions,
                                                   List<String> alreadyExistPermissions) {
        StringBuilder messageBuilder = new StringBuilder();

        if (!addedPermissions.isEmpty()) {
            messageBuilder.append("Quyền hạn ");
            messageBuilder.append(String.join(", ", addedPermissions));
            messageBuilder.append(" đã được thêm vào vai trò ").append(roleName).append(". ");
        }

        if (!notAddedPermissions.isEmpty()) {
            messageBuilder.append("Quyền hạn ");
            messageBuilder.append(String.join(", ", notAddedPermissions));
            messageBuilder.append(" không được thêm vào vai trò ").append(roleName);
            messageBuilder.append(" vì không tồn tại trên hệ thống. ");
        }

        if (!alreadyExistPermissions.isEmpty()) {
            messageBuilder.append("Quyền hạn ");
            messageBuilder.append(String.join(", ", alreadyExistPermissions));
            messageBuilder.append(" đã tồn tại trong vai trò ").append(roleName).append(". ");
        }

        return messageBuilder.toString().trim();
    }
}
