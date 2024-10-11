package com.server.wordwaves.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PascalCaseNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        return convertToPascalCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        return convertToPascalCase(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertToPascalCase(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
        return convertToPascalCase(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToPascalCase(name);
    }

    private Identifier convertToPascalCase(Identifier name) {
        if (name == null || name.getText() == null) {
            return name;
        }
        String pascalCase = toPascalCase(name.getText());
        return new Identifier(pascalCase, name.isQuoted());
    }

    private String toPascalCase(String text) {
        if (text.contains("_")) {
            String[] parts = text.split("_");
            StringBuilder pascalCase = new StringBuilder();
            for (String part : parts) {
                pascalCase
                        .append(part.substring(0, 1).toUpperCase())
                        .append(part.substring(1).toLowerCase());
            }
            return pascalCase.toString();
        } else {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
    }
}
